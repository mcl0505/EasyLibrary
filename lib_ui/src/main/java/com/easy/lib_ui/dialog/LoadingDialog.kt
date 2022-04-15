package com.easy.lib_ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.animation.Animation
import android.view.animation.RotateAnimation
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

//        val animation: Animation = RotateAnimation(
//            0f,
//            360f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f
//        )
//
//        animation.duration = 2000
//        animation.repeatCount = 10
//        animation.fillAfter = true
//        imageView.startAnimation(animation)

    }

    fun showDialog(context: Context, isCancel: Boolean) {
        if (context is Activity) {
            if (context.isFinishing) {
                return
            }
        }

        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(context, isCancel)

        }

       findViewById<LottieAnimationView>(R.id.loadingView).playAnimation()
        loadingDialog?.show()
    }

    fun dismissDialog() {
        findViewById<LottieAnimationView>(R.id.loadingView).cancelAnimation()
        loadingDialog?.dismiss()
    }

}