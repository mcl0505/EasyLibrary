package com.easy.lib_ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.easy.lib_ui.IView
import com.easy.lib_ui.R
import com.easy.lib_ui.TitleBar
import com.easy.lib_ui.dialog.LoadingDialog
import com.easy.lib_ui.mvvm.model.BaseModel
import com.easy.lib_ui.mvvm.viewmodel.BaseViewModel
import com.easy.lib_ui.ui.MultiStateView
import com.easy.lib_util.LogUtil
import com.easy.lib_util.Utils
import com.easy.lib_util.bus.LiveDataBus
import com.easy.lib_util.executor.AppExecutorsHelper
import com.easy.lib_util.ext.visibleOrGone
import com.easy.lib_util.receiver.NetStateReceiver
import com.imyyq.mvvm.base.IActivityResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class EasyFragment<V : ViewBinding, VM : BaseViewModel>(
    private val sharedViewModel: Boolean = false,
) : Fragment(), IView<V, VM>{
    //Activity 标识
    open val TAG: String get() = this::class.java.simpleName
    protected lateinit var mContext: Context
    protected lateinit var mActivity: AppCompatActivity
    private lateinit var mLoadingDialog: LoadingDialog
    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM
    protected lateinit var mView: View
    //标题
    protected lateinit var mTitlebar: TitleBar
    //内容
    private lateinit var mViewContent: FrameLayout
    //当前页面展示布局状态

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mView = inflater.inflate(R.layout.activity_root, container, false)
        return mView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewAndViewModel()
        initParam()
        initTitle()
        initUiChangeLiveData()
        initData()
        initViewObservable()

    }

    fun initTitle() {
        mTitlebar = mView.findViewById(R.id.titleBar)
        mViewContent = mView.findViewById(R.id.frame_content)
        mBinding = initBinding(layoutInflater, null)
        mTitlebar.apply {
            visibleOrGone(!setTitleText().isNullOrEmpty())
            tvTitleCenter.text = setTitleText()
        }
    }

    /**
     * 默认不显示标题
     */
    open fun setTitleText(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = (activity as AppCompatActivity?)!!
        mLoadingDialog = LoadingDialog(mActivity, false)
    }

    @CallSuper
    override fun initViewAndViewModel() {
        mViewModel = if (sharedViewModel) {
            initViewModel(requireActivity())
        } else {
            initViewModel(this)
        }
        // 让 vm 可以感知 v 的生命周期
        lifecycle.addObserver(mViewModel)

        mLoadingDialog.onCancelLoadingDialog = {
            mViewModel.cancelConsumingTask()
        }
    }


    @CallSuper
    override fun initUiChangeLiveData() {
        //网络状态监听
//        LiveDataBus.observe<NetStateReceiver.NetState>(this, "NetStateReceiver", {
//            LogUtil.d("NetStateReceiver.NetState=${it}")
//            when (it) {
//                NetStateReceiver.NetState.CONNECT_NO -> {
//                    initMultiStateView(MultiStateView.STATE_NET_ERROR)
//                }
//                else -> {
//                    initMultiStateView(MultiStateView.STATE_LOADING)
//                    AppExecutorsHelper.postDelayed({
//                        initMultiStateView(MultiStateView.STATE_SUCCESS)
//                    })
//                }
//            }
//        })
    }


    /**
     * 如果加载中对话框可手动取消，并且开启了取消耗时任务的功能，则在取消对话框后调用取消耗时任务
     */
//    @CallSuper
//    override fun onCancelLoadingDialog() = mViewModel.cancelConsumingTask()

    override fun onDestroyView() {
        super.onDestroyView()

        // 通过反射，解决内存泄露问题
        GlobalScope.launch {
            var clz: Class<*>? = this@EasyFragment.javaClass
            while (clz != null) {
                // 找到 mBinding 所在的类
                if (clz == EasyFragment::class.java) {
                    try {
                        val field = clz.getDeclaredField("mBinding")
                        field.isAccessible = true
                        field.set(this@EasyFragment, null)
                    } catch (ignore: Exception) {
                    }
                }
                clz = clz.superclass
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // 界面销毁时移除 vm 的生命周期感知
        if (this::mViewModel.isInitialized) {
            lifecycle.removeObserver(mViewModel)
        }
        removeLiveDataBus(this)
    }

}