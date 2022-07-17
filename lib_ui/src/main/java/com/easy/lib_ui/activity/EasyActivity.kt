package com.easy.lib_ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.easy.lib_util.app.EasyApplication
import com.easy.lib_ui.IView
import com.easy.lib_ui.R
import com.easy.lib_ui.TitleBar
import com.easy.lib_ui.dialog.LoadingDialog
import com.easy.lib_ui.mvvm.TokenInvalidLiveData
import com.easy.lib_ui.mvvm.viewmodel.BaseViewModel
import com.easy.lib_util.app.AppManager
import com.easy.lib_util.bar.StatusBarUtils
import com.easy.lib_util.ext.observe
import com.easy.lib_util.ext.singleClick
import com.easy.lib_util.ext.visibleOrGone
import com.easy.lib_util.ext.yes
import com.easy.lib_util.soft.SoftInputModelUtil
import com.easy.lib_util.toast.toast
import android.widget.FrameLayout
import com.easy.lib_util.bus.LiveDataBus


/**
 * Activity 基类  包含
 * 1:标题栏
 * 2:ViewModel 配置
 * 3:ViewBindiing 配置
 * 4:页面状态配置
 */
abstract class EasyActivity<V : ViewBinding, VM : BaseViewModel> :
    AppCompatActivity(R.layout.activity_root), IView<V, VM>{
    //Activity 标识
    open val TAG: String get() = this::class.java.simpleName
    protected lateinit var mContext: Context
    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM
    protected lateinit var mViewContent: FrameLayout
    //标题
    protected lateinit var mTitlebar: TitleBar

    private lateinit var mLoadingDialog: LoadingDialog

    //true=黑色  false=白色
    open val isDark get() = false

    //true=禁用重力感应  false=启用重力感应
    open val isNoSensor get() = false

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
        mViewContent = findViewById(R.id.frame_content)
        mViewContent.addView(mBinding.root)
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
    }

    open fun onLayoutBefore() {}

    abstract override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V

    private fun initTitle() {
        mTitlebar = findViewById(R.id.titleBar)
        mTitlebar.apply {
            titleLine.visibleOrGone(false)
            visibleOrGone(!setTitleText().isNullOrEmpty())
            tvTitleCenter.text = setTitleText()
            imgTitleLeft.singleClick { finish() }
        }
    }

    /**
     * 默认不显示标题
     */
    open fun setTitleText(): String = ""

    @CallSuper
    override fun initViewAndViewModel() {
        mViewModel = initViewModel(this)
        // 让 vm 可以感知 v 的生命周期
        lifecycle.addObserver(mViewModel)

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

    }

    @CallSuper
    override fun initUiChangeLiveData() {
    }

    protected var time: Long = 0

    override fun onDestroy() {
        super.onDestroy()

        // 界面销毁时移除 vm 的生命周期感知
        if (this::mViewModel.isInitialized) {
            lifecycle.removeObserver(mViewModel)
        }

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


}