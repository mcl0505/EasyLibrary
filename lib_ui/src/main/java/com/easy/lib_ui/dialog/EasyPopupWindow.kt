package com.easy.lib_ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.easy.lib_ui.R
import com.easy.lib_util.ext.getColor
import org.jetbrains.anko.layoutInflater

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2021/3/23
 * 功能描述：
 */

//   使用方式一
// class HintPopupWindow(context: Context) : BindPopupWindow<DialogHintBinding>(context,R.layout.dialog_hint){
//    override fun main() {
//        mPpopupBinding.dialogHintTitle.singleClick { it.text.toString().toast() }
//    }
//
//}


//    使用方式二
//BindPopupWindow<DialogHintBinding>(mContext, R.layout.dialog_hint, {
//    it.dialogHintTitle.singleClick { it.text.toString().toast() }
//}).show(it, TypeGravity.BOTTOM_CENTER)


open class EasyPopupWindow<DB : ViewDataBinding>(context: Context, layoutId: Int) {
    lateinit var mPpopupBinding: DB
    private var mPopupWindow: PopupWindow? = null
    private var mBackgroundDrawable: Drawable? = null

    //是否能响应弹框中的点击事件
    private var mTouchable = true
    private var mView: View? = null
    private var mOffsetX = 0
    private var mOffsetY = 0

    lateinit var onViewClick:(mViewBinding:DB)->Unit

    //显示的位置
    @TypeGravity
//    private var mGravity = 0
    //相对于的控件
//    private var mTarget: View? = null
    //动画
    private var mAnimationStyle = 0

    init {
        if (mPopupWindow == null) {
            mPopupWindow = PopupWindow(context)
        }

        if (mView != null) {
            mPopupWindow!!.contentView = mView
        } else if (layoutId != -1) {
            mPpopupBinding = DataBindingUtil.inflate(context.layoutInflater, layoutId, null, false)
            mView = mPpopupBinding.root
            mPopupWindow!!.contentView = mView
        }


    }

    /**
     * @param mGravity 弹框所显示的位置
     * @param mTarget 弹框显示的相对控件
     */
    fun show(mTarget: View,@TypeGravity mGravity: Int = 0) {
        if (::onViewClick.isInitialized)onViewClick.invoke(mPpopupBinding)
        main()
        mPopupWindow!!.width = setWidth()
        if (setHeight() != 0) mPopupWindow!!.height = setHeight()
        mPopupWindow!!.setBackgroundDrawable(ColorDrawable(setBackgroundColor()))
        mPopupWindow!!.isOutsideTouchable = canCancel()
        mPopupWindow!!.animationStyle = mAnimationStyle
        mPopupWindow!!.isTouchable = mTouchable
        val locations = IntArray(2)
        mTarget!!.getLocationOnScreen(locations)
        val left = locations[0]
        val top = locations[1]
        mPopupWindow!!.contentView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        val popupWidth = mPopupWindow!!.contentView.measuredWidth
        val popupHeight = mPopupWindow!!.contentView.measuredHeight
        val targetWidth = mTarget!!.width
        val targetHeight = mTarget!!.height
        when (mGravity) {
            TypeGravity.TOP_LEFT -> mPopupWindow!!.showAtLocation(mTarget, Gravity.NO_GRAVITY, left + mOffsetX, top - popupHeight + mOffsetY)
            TypeGravity.TOP_CENTER -> {
                val offsetX = (targetWidth - popupWidth) / 2
                mPopupWindow!!.showAtLocation(mTarget, Gravity.NO_GRAVITY, left + offsetX + mOffsetX, top - popupHeight + mOffsetY)
            }
            TypeGravity.TOP_RIGHT -> mPopupWindow!!.showAtLocation(mTarget, Gravity.NO_GRAVITY, left + targetWidth - popupWidth + mOffsetX, top - popupHeight + mOffsetY)
            TypeGravity.CENTER -> {
                val x = left + (targetWidth - popupWidth) / 2 + mOffsetX
                val y = top + (targetHeight - popupHeight) / 2 + mOffsetY
                mPopupWindow!!.showAtLocation(mTarget, Gravity.NO_GRAVITY, x, y)
            }
            TypeGravity.CENTER_LEFT_TOP -> mPopupWindow!!.showAtLocation(mTarget, Gravity.NO_GRAVITY, left + mOffsetX, top + mOffsetY)
            TypeGravity.CENTER_LEFT_BOTTOM -> mPopupWindow!!.showAtLocation(mTarget, Gravity.NO_GRAVITY, left + mOffsetX, top + (targetWidth - popupHeight) + mOffsetY)
            TypeGravity.CENTER_RIGHT_BOTTOM -> mPopupWindow!!.showAtLocation(mTarget, Gravity.NO_GRAVITY, left + (targetWidth - popupWidth) + mOffsetX, top + (targetHeight - popupHeight) + mOffsetY)
            TypeGravity.CENTER_RIGHT_TOP -> mPopupWindow!!.showAtLocation(mTarget, Gravity.NO_GRAVITY, left + (targetWidth - popupWidth) + mOffsetX, top + mOffsetY)
            TypeGravity.BOTTOM_LEFT -> mPopupWindow!!.showAsDropDown(mTarget, mOffsetX, mOffsetY)
            TypeGravity.BOTTOM_CENTER -> {
                val offsetX1 = (targetWidth - popupWidth) / 2
                mPopupWindow!!.showAsDropDown(mTarget, offsetX1 + mOffsetX, mOffsetY)
            }
            TypeGravity.BOTTOM_RIGHT -> mPopupWindow!!.showAsDropDown(mTarget, targetWidth - popupWidth + mOffsetX, mOffsetY)
            TypeGravity.FROM_BOTTOM -> {
                mPopupWindow!!.width = setWidth()
                mPopupWindow!!.showAtLocation(mTarget, Gravity.BOTTOM, mOffsetX, mOffsetY)
            }
            TypeGravity.FROM_TOP -> {
                mPopupWindow!!.width = setWidth()
                mPopupWindow!!.showAtLocation(mTarget, Gravity.TOP, mOffsetX, mOffsetY)
            }
        }

    }

    fun dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow!!.dismiss()
        }
    }

    //设置布局高  LinearLayout.LayoutParams.MATCH_PARENT   LinearLayout.LayoutParams.WRAP_CONTENT
    protected open fun setHeight(): Int {
        return LinearLayout.LayoutParams.WRAP_CONTENT
    }

    //设置布局宽  LinearLayout.LayoutParams.MATCH_PARENT   LinearLayout.LayoutParams.WRAP_CONTENT
    protected open fun setWidth(): Int {
        return LinearLayout.LayoutParams.MATCH_PARENT
    }

    //是否可以点击外部取消弹框  true=可以  false=不可以
    protected open fun canCancel(): Boolean {
        return true
    }

    //设置背景颜色
    protected open fun setBackgroundColor(): Int {
        return R.color.black_80.getColor()
    }

    open fun main() {}

}