package com.easy.lib_ui.dialog

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.easy.lib_ui.R
import com.easy.lib_util.app.AppManager

class LoadingDialog : Dialog {

    private var loadingDialog: LoadingDialog? = null

    constructor(context: Context, canNotCancel: Boolean) : super(
        context,
        R.style.LoadingDialog
    ) {
        setContentView(R.layout.layout_loading_view)
    }

    /**
     * @param isCancel 是否能够外部取消弹框  true=正常取消弹框  false=取消弹框的同时取消ViewModel中的网络请求事件
     * @param msg 弹框提示信息
     */
    fun showDialog(isCancel: Boolean = false,msg:String = "加载中") {

        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(AppManager.getContext(), isCancel)
        }

        if (!isCancel){
            loadingDialog?.setOnCancelListener { onCancelLoadingDialog() }
        }

        loadingDialog?.findViewById<TextView>(R.id.message)?.text = msg
        loadingDialog?.findViewById<LottieAnimationView>(R.id.loadingView)?.playAnimation()

        loadingDialog?.show()
    }


    fun dismissDialog() {
        loadingDialog?.findViewById<LottieAnimationView>(R.id.loadingView)?.cancelAnimation()
        loadingDialog?.dismiss()
    }

    /**
     * 加载中对话框被用户手动取消了，则回调此方法
     */
    lateinit var onCancelLoadingDialog:()->Unit

}