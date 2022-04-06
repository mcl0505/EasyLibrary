package com.easy.lib_util.dsl.view

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.LINEAR_GRADIENT
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.view.View
import com.easy.lib_util.R
import com.easy.lib_util.dsl.text.DslSpanBuilder
import com.easy.lib_util.dsl.text.DslSpannableStringBuilderImpl
import com.easy.lib_util.ext.getColor
import com.easy.lib_util.ext.toDp

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/04/02
 *   功能描述:
 */


inline fun View.renderDrawable(init: DslViewDrawableBuilder.() -> Unit) {
    //具体实现类
    val drawableBuilder = DslViewDrawableBuilderImpl()
    drawableBuilder.init()
    this.background = drawableBuilder.build()
}

//添加操作接口
interface DslViewDrawableBuilder {
    //设置 获取Drawable
    fun setDrawable(method: (DslViewBuilder.() -> Unit)? = null)

    //设置 设置渐变
    fun setGradientLinear(method: (DslViewBuilder.() -> Unit)? = null)
}

//属性
interface DslViewBuilder {
    /**
     * 设置背景颜色
     */
    fun setSolidColor(color: Int)

    /**
     * 设置圆角
     */
    fun setCornerSize(size: Float)

    /**
     * 设置不同的圆角  [左下,左上,右上,右下]
     */
    fun mViewCornerRadii(size: FloatArray)

    /**
     * 边框
     */
    fun setStroke(size: Int,color: Int)

    /**
     * 线性渐变
     * @param colorArray 颜色数组
     * @param gradientType 渐变方式
     *        LEFT_RIGHT ===>  左到右  颜色数组中的顺序即是渐变顺序
     *        RIGHT_LEFT ===>  右到左  颜色数组中的顺序即是渐变顺序
     *        BOTTOM_TOP ===>  下到上  颜色数组中的顺序即是渐变顺序
     *        TOP_BOTTOM ===>  上到下  颜色数组中的顺序即是渐变顺序
     */
    fun setGradientLinear(colorArray: IntArray,gradientType:GradientDrawable.Orientation?=GradientDrawable.Orientation.LEFT_RIGHT)
}

//属性实现
class DslViewBuilderImp : DslViewBuilder {

    var mViewSolidColor: Int? = R.color.ps_color_transparent.getColor()
    var mViewCornerSize: Float? = 0f
    var mViewCornerRadii: FloatArray? = floatArrayOf(0f,0f,0f,0f)
    var mViewGradient: IntArray? = intArrayOf() //渐变颜色值
    var mViewGradientType: GradientDrawable.Orientation? = GradientDrawable.Orientation.RIGHT_LEFT
    var mViewStrokeSize: Int? = 0
    var mViewStrokeColor: Int? = R.color.ps_color_transparent.getColor()

    override fun setSolidColor(color: Int) {
        mViewSolidColor = color
    }

    override fun setCornerSize(size: Float) {
        mViewCornerSize = size
    }

    override fun mViewCornerRadii(size: FloatArray) {
        mViewCornerRadii = size
    }


    override fun setStroke(size: Int,color: Int) {
        mViewStrokeSize = size
        mViewStrokeColor = color
    }

    override fun setGradientLinear(colorArray: IntArray,gradientType:GradientDrawable.Orientation?) {
        mViewGradient = colorArray
        mViewGradientType = gradientType
    }

}

class DslViewDrawableBuilderImpl : DslViewDrawableBuilder {
    private val drawable = GradientDrawable()

    override fun setDrawable(method: (DslViewBuilder.() -> Unit)?) {
        val mDrawableBuilder = DslViewBuilderImp()
        method?.let { mDrawableBuilder.it() }

        //背景颜色
        drawable.setColor(mDrawableBuilder.mViewSolidColor!!)
        if (mDrawableBuilder.mViewCornerSize!!>0){
            drawable.cornerRadius = mDrawableBuilder.mViewCornerSize!!.toDp()
        }else {
            drawable.cornerRadii = mDrawableBuilder.mViewCornerRadii!!
        }

        drawable.setStroke(mDrawableBuilder.mViewStrokeSize!!.toDp(),mDrawableBuilder.mViewStrokeColor!!)
    }

    override fun setGradientLinear(method: (DslViewBuilder.() -> Unit)?) {
        val mDrawableBuilder = DslViewBuilderImp()
        method?.let { mDrawableBuilder.it() }

        if (mDrawableBuilder.mViewCornerSize!!>0){
            drawable.cornerRadius = mDrawableBuilder.mViewCornerSize!!.toDp()
        }else {
            drawable.cornerRadii = mDrawableBuilder.mViewCornerRadii!!
        }

        //设置渐变方向
        // LINEAR_GRADIENT  线性从左倒右  颜色数组中的顺序即是渐变顺序
        drawable.gradientType = LINEAR_GRADIENT
        mDrawableBuilder.mViewGradientType?.let {
            drawable.orientation = it
            drawable.colors = mDrawableBuilder.mViewGradient!!
        }
    }

    fun build(): GradientDrawable {
        return drawable
    }


}