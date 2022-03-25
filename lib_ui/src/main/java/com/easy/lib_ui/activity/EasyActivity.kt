package com.easy.lib_ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.easy.lib_util.app.EasyApplication
import com.easy.lib_ui.IView
import com.easy.lib_ui.R
import com.easy.lib_ui.TitleBar
import com.easy.lib_ui.dialog.LoadingDialog
import com.easy.lib_ui.mvvm.TokenInvalidLiveData
import com.easy.lib_ui.mvvm.model.BaseModel
import com.easy.lib_ui.mvvm.viewmodel.BaseViewModel
import com.easy.lib_ui.ui.MultiStateView
import com.easy.lib_util.Utils
import com.easy.lib_util.app.AppManager
import com.easy.lib_util.bar.StatusBarUtils
import com.easy.lib_util.executor.AppExecutorsHelper
import com.easy.lib_util.ext.observe
import com.easy.lib_util.ext.singleClick
import com.easy.lib_util.ext.visibleOrGone
import com.easy.lib_util.ext.yes
import com.easy.lib_util.soft.SoftInputModelUtil
import com.easy.lib_util.toast.toast
import com.imyyq.mvvm.base.IActivityResult
import com.imyyq.mvvm.base.IArgumentsFromIntent
import com.easy.lib_util.receiver.NetStateReceiver

import android.net.ConnectivityManager

import android.content.IntentFilter
import com.easy.lib_util.LogUtil
import com.easy.lib_util.bus.LiveDataBus


/**
 * Activity 基类  包含
 * 1:标题栏
 * 2:ViewModel 配置
 * 3:ViewBindiing 配置
 * 4:页面状态配置
 */
abstract class EasyActivity<V : ViewBinding, VM : BaseViewModel<out BaseModel>> :
    AppCompatActivity(R.layout.activity_root),
    IView<V, VM>, IActivityResult, IArgumentsFromIntent {
    //Activity 标识
    open val TAG: String get() = this::class.java.simpleName
    protected lateinit var mContext: Context
    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM

    //标题
    protected lateinit var mTitlebar: TitleBar

    //内容
    private lateinit var mViewContent: MultiStateView

    //页面跳转返回数据
    private lateinit var mStartActivityForResult: ActivityResultLauncher<Intent>

    private lateinit var mLoadingDialog: LoadingDialog

    private lateinit var mStateReceiver: NetStateReceiver

    //true=黑色  false=白色
    open val isDark get() = false

    //true=使用多状态布局  false=不使用
    open val isMultiState get() = true

    //true=禁用重力感应  false=启用重力感应
    open val isNoSensor get() = false

    //当前页面展示布局状态
    open var mViewCurrentState = MultiStateView.STATE_LOADING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        //操作等待提示框
        mLoadingDialog = LoadingDialog(this, false)
        //全屏 透明状态栏
        StatusBarUtils.setStatusBarTransparent(this)
        //设置默认状态栏字体为黑色
        StatusBarUtils.setStateBarTextColor(this, isDark)
        (isNoSensor).yes {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        }
        onLayoutBefore()
        setContentView(R.layout.activity_root)
        mBinding = initBinding(layoutInflater, null)
        //初始化ViewModel
        initViewAndViewModel()
        //初始化参数
        initParam()
        //初始化标题
        initTitle()
        //UI 状态改变
        initUiChangeLiveData()
        //基础业务逻辑
        initData()
        //事件观察
        initViewObservable()
        //基础事件观察
        initBaseLiveData()
        //页面跳转数据回调注册监听
        initStartActivityForResult()
    }

    open fun onLayoutBefore() {}

    abstract override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V

    private fun initTitle() {
        mTitlebar = findViewById(R.id.titleBar)
        mViewContent = findViewById(R.id.frame_content)
        mViewContent.successView = mBinding.root
        mTitlebar.apply {
            titleLine.visibleOrGone(false)
            visibleOrGone(!setTitleText().isNullOrEmpty())
            tvTitleCenter.text = setTitleText()
            imgTitleLeft.singleClick { onBackPressed() }
        }
        //动态注册网络状态广播  跟随随Activity生命周期
        registerReceiver()
    }

    private fun initMultiStateView(state: Int) {
        mViewCurrentState = state
        AppExecutorsHelper.uiHandler {
            if (isMultiState) {
                when (state) {
                    MultiStateView.STATE_LOADING -> {
                        mViewContent.showLoading()
                    }
                    MultiStateView.STATE_SUCCESS -> {
                        mViewContent.showSuccess()
                    }
                    MultiStateView.STATE_EMPTY -> {
                        mViewContent.showEmpty()
                    }
                    MultiStateView.STATE_NET_ERROR -> {
                        mViewContent.showNetError()
                    }
                    MultiStateView.STATE_UNKNOWN -> {
                        mViewContent.showUnKnown()
                    }
                    MultiStateView.STATE_NEW_STATE -> {
                        mViewContent.showNewStateView()
                    }
                }
            } else {
                mViewContent.showSuccess()
            }
        }
    }

    /**
     * 默认不显示标题
     */
    open fun setTitleText(): String = ""

    @CallSuper
    override fun initViewAndViewModel() {
        mViewModel = initViewModel(this)
        mViewModel.mIntent = getArgumentsIntent()
        // 让 vm 可以感知 v 的生命周期
        lifecycle.addObserver(mViewModel)

        mLoadingDialog.onCancelLoadingDialog = {
            mViewModel.cancelConsumingTask()
        }

    }

    /**
     * 基础事件监听
     */
    private fun initBaseLiveData(){
        //Token 失效监听
        observe(TokenInvalidLiveData, {
            if (it) {
                //两次触发事件不能小于2秒
                if (System.currentTimeMillis() - time > 2000) {
                    TokenInvalidLiveData.postValue(false)

                    if (EasyApplication.instance.getInvalidActivity() == null) {
                        "请设置登录失效跳转的界面".toast()
                    } else {
                        time = System.currentTimeMillis()
                        val intent = Intent(this, EasyApplication.instance.getInvalidActivity())
                        AppManager.finishAllActivity()
                        this.startActivity(intent)
                    }


                }
            }
        })
        //网络状态监听
        LiveDataBus.observe<NetStateReceiver.NetState>(this,"NetStateReceiver",{
            LogUtil.d("NetStateReceiver.NetState=${it}")
            when(it){
                NetStateReceiver.NetState.CONNECT_NO -> {
                    initMultiStateView(MultiStateView.STATE_NET_ERROR)
                }
                else -> {
                    initMultiStateView(MultiStateView.STATE_LOADING)
                    AppExecutorsHelper.postDelayed({
                        initMultiStateView(MultiStateView.STATE_SUCCESS)
                    })
                }
            }
        })
    }

    @CallSuper
    override fun initUiChangeLiveData() {
        //界面跳转
        mViewModel.mUiChangeLiveData.initStartAndFinishEvent()

        fun setResult(pair: Pair<Int?, Intent?>) {
            pair.first?.let { resultCode ->
                val intent = pair.second
                if (intent == null) {
                    setResult(resultCode)
                } else {
                    setResult(resultCode, intent)
                }
            }
        }

        // vm 可以结束界面
        LiveDataBus.observe<Pair<Int?, Intent?>>(
            this,
            mViewModel.mUiChangeLiveData.finishEvent!!,
            Observer {
                setResult(it)
                finish()
            },
            true
        )
        LiveDataBus.observe<Pair<Int?, Intent?>>(
            this,
            mViewModel.mUiChangeLiveData.setResultEvent!!,
            Observer { setResult(it) },
            true
        )
        // vm 可以启动界面
        LiveDataBus.observe<Class<out Activity>>(
            this,
            mViewModel.mUiChangeLiveData.startActivityEvent!!,
            Observer {
                startActivity(it)
            },
            true
        )

        LiveDataBus.observe<Pair<Class<out Activity>, MutableMap<String, *>>>(this,
            mViewModel.mUiChangeLiveData.startActivityWithMapEvent!!,
            Observer {
                startActivity(it?.first, it?.second)
            },
            true
        )
        // vm 可以启动界面，并携带 Bundle，接收方可调用 getBundle 获取
        LiveDataBus.observe<Pair<Class<out Activity>, Bundle?>>(this,
            mViewModel.mUiChangeLiveData.startActivityEventWithBundle!!,
            Observer {
                startActivity(it?.first, bundle = it?.second)
            },
            true
        )


        //页面跳转回调
        mViewModel.mUiChangeLiveData.initStartActivityForResultEvent()

        // vm 可以启动界面
        LiveDataBus.observe<Class<out Activity>>(
            this,
            mViewModel.mUiChangeLiveData.startActivityForResultEvent!!,
            Observer {
                startActivityForResult(it)
            },
            true
        )
        // vm 可以启动界面，并携带 Bundle，接收方可调用 getBundle 获取
        LiveDataBus.observe<Pair<Class<out Activity>, Bundle?>>(
            this,
            mViewModel.mUiChangeLiveData.startActivityForResultEventWithBundle!!,
            Observer {
                startActivityForResult(it?.first, bundle = it?.second)
            },
            true
        )
        LiveDataBus.observe<Pair<Class<out Activity>, MutableMap<String, *>>>(
            this,
            mViewModel.mUiChangeLiveData.startActivityForResultEventWithMap!!,
            Observer {
                startActivityForResult(it?.first, it?.second)
            },
            true
        )


    }

    protected var time: Long = 0

    /**
     * 跳转封装
     * @param clz 目标界面
     * @param map 参数
     * @param bundle 参数
     */
    fun startActivity(clz: Class<out Activity>?, map: MutableMap<String, *>? = null, bundle: Bundle? = null) {
        startActivity(Utils.getIntentByMapOrBundle(this, clz, map, bundle))
    }

    /**
     * 跳转封装  可接受返回数据
     * @param clz 目标界面
     * @param map 参数
     * @param bundle 参数
     */
    fun startActivityForResult(clz: Class<out Activity>?, map: MutableMap<String, *>? = null, bundle: Bundle? = null) {
        mStartActivityForResult.launch(Utils.getIntentByMapOrBundle(this, clz, map, bundle))
    }

    //必须先在OnCreate 中注册
    private fun initStartActivityForResult() {
        if (!this::mStartActivityForResult.isInitialized) {
            mStartActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val data = it.data ?: Intent()
                when (it.resultCode) {
                    Activity.RESULT_OK -> {
                        onActivityResultOk(data)
                        if (this::mViewModel.isInitialized) {
                            mViewModel.onActivityResultOk(data)
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        onActivityResultCanceled(data)
                        if (this::mViewModel.isInitialized) {
                            mViewModel.onActivityResultCanceled(data)
                        }
                    }
                    else -> {
                        onActivityResult(it.resultCode, data)
                        if (this::mViewModel.isInitialized) {
                            mViewModel.onActivityResult(it.resultCode, data)
                        }
                    }
                }
            }
        }


    }

    /**
     * 通过 [BaseViewModel.startActivity] 传递 bundle，在这里可以获取
     */
    final override fun getBundle(): Bundle? {
        return intent.extras
    }

    final override fun getArgumentsIntent(): Intent? {
        return intent
    }

    override fun onDestroy() {
        super.onDestroy()

        // 界面销毁时移除 vm 的生命周期感知
        if (this::mViewModel.isInitialized) {
            lifecycle.removeObserver(mViewModel)
        }
        //移除网络状态监听广播
        unregisterReceiver(mStateReceiver)

        removeLiveDataBus(this)
    }

    /**
     * APP字体大小，不随系统的字体大小变化而变化
     */
    override fun getResources(): Resources? {
        val res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

    //点击键盘外部隐藏键盘
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            //当键盘弹出是，点击键盘之外的区域隐藏键盘
            val view = currentFocus
            hideKeyboard(ev, view)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun hideKeyboard(event: MotionEvent, view: View?) {
        try {
            if (view != null && view is EditText) {
                val location = intArrayOf(0, 0)
                view.getLocationInWindow(location)
                val left = location[0]
                val top = location[1]
                val right = (left
                        + view.getWidth())
                val bootom = top + view.getHeight()
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.rawX < left || event.rawX > right || event.y < top || event.rawY > bootom
                ) {
                    SoftInputModelUtil.hideSoftInput(view)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var lastTime: Long = 0
    private val intervalTime = 1000 * 2.toLong()

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ("MainActivity" == TAG) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastTime < intervalTime) {
                    AppManager.appExit()
                } else {
                    lastTime = currentTime
                    "再按一次退出程序".toast()
                    return false
                }
            } else {
                return super.onKeyDown(keyCode, event)
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun registerReceiver() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        mStateReceiver = NetStateReceiver()
        registerReceiver(mStateReceiver, filter)
    }

}