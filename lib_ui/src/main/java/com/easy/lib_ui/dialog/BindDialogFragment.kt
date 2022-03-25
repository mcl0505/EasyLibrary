package com.easy.lib_ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.easy.lib_ui.R


/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：BaseDralogFragment
 * 创建时间：2020/8/11
 * 功能描述： DialogFragment的基类封装
 */


//class HintDialog : BindDialogFragment<DialogHintBinding>(R.layout.dialog_hint) {
//    override fun main(savedInstanceState: Bundle?) {
//        mDialogBinding.dialogHintTitle.singleClick { it.text.toString().toast() }
//    }
//}
open class BindDialogFragment<DB : ViewDataBinding>(val layoutId: Int) : DialogFragment() {
    @JvmField
    protected var TAG = this.javaClass.simpleName

    lateinit var onViewClick:(mView:DB)->Unit

    @JvmField
    protected var mContext: Context? = null
    lateinit var mDialogBinding: DB

    protected lateinit var mRootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDialogBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mDialogBinding.root
    }

    //执行在Fragment 的 onGetLayoutInflater 中
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mContext = activity
        mRootView = LayoutInflater.from(mContext).inflate(layoutId, null)
        val dialog = Dialog(mContext!!, getDialogStyle())
        dialog.setContentView(mRootView)
        dialog.setCancelable(canCancel())
        dialog.setCanceledOnTouchOutside(canCancel())
        val window = dialog.window
        window!!.setGravity(setGravity())
        window.setWindowAnimations(getDialogAnim())
        window.setLayout(setWidth(), setHeight())
        return dialog
    }

    open fun getDialogStyle(): Int = R.style.dialog_style

    protected open fun setWidth(): Int {
        return LinearLayout.LayoutParams.MATCH_PARENT
    }

    protected open fun setHeight(): Int {
        return LinearLayout.LayoutParams.WRAP_CONTENT
    }

    protected open fun setGravity(): Int {
        return Gravity.BOTTOM
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        main(savedInstanceState)
        if (::onViewClick.isInitialized)onViewClick.invoke(mDialogBinding)

    }

    /**
     * 处理具体的业务逻辑
     * @param savedInstanceState
     */
    open fun main(savedInstanceState: Bundle?) {}


    /**
     * 弹框进入与消失动画
     */
    open fun getDialogAnim(): Int = R.style.dialogAnimBottom

    protected open fun canCancel(): Boolean {
        return true
    }


    open fun show(manager: FragmentManager?) {
        super.show(manager!!, TAG)
    }
}