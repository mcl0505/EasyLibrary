package com.easy.lib_util.dsl

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView

/**
 *   公司名称: xxx
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/03/25
 *   功能描述:
 *
 *   binding.name.buildRender {
addText(item.name){
setColor("#000000")
setSize(12)
}
addImg(R.drawable.ic_android.getDrawable())
}
 */

interface DslTextRenderBuilder {
    //增加一段文字
    fun addText(text: String, method: (DslTextAttrBuilder.() -> Unit)? = null)
    //增加一张图片
    fun addImg(img: Drawable, method: (DslTextAttrBuilder.() -> Unit)? = null)
}

interface DslTextAttrBuilder {
    //设置文字颜色
    fun setColor(color: String)
    //设置文字大小
    fun setSize(size: Int)
    //设置图片高
    fun setImageHeight(size: Int)
    //设置图片宽
    fun setImageWight(size: Int)
    //设置点击事件
    fun onClick(useUnderLine: Boolean = true, onClick: (View) -> Unit)
}


//为 TextView 创建扩展函数，其参数为接口的扩展函数
fun TextView.buildRender(init: DslTextRenderBuilder.() -> Unit) {
    //具体实现类
    val spanStringBuilderImpl = DslTextRenderBuilderImpl()
    spanStringBuilderImpl.init()
    movementMethod = LinkMovementMethod.getInstance()
    //通过实现类返回SpannableStringBuilder
    text = spanStringBuilderImpl.build()
}

class DslTextRenderBuilderImpl : DslTextRenderBuilder{
    private val builder = SpannableStringBuilder()
    //记录上次添加文字后最后的索引值
    var lastIndex: Int = 0
    override fun addText(text: String, method: (DslTextAttrBuilder.() -> Unit)?) {

        val start = lastIndex
        builder.append(text)
        val spanBuilder = DslTextAttrBuilderImpl()
        spanBuilder.apply {
            foregroundColorSpan?.let {
                builder.setSpan(it, start, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            fontSizeSpan?.let {
                builder.setSpan(it, start, lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

    }

    override fun addImg(img: Drawable, method: (DslTextAttrBuilder.() -> Unit)?) {
        builder.append("   ")
        val spanBuilder = DslTextAttrBuilderImpl()
        spanBuilder.apply {
            if (fontSizeSpanHeight == null && fontSizeSpanWight == null){
                img.setBounds(0, 0, img.intrinsicWidth, img.intrinsicHeight)
            }else {
                img.setBounds(0, 0, spanBuilder.fontSizeSpanWight!!, spanBuilder.fontSizeSpanHeight!!)
            }
        }

        builder.setSpan(VerticalImageSpan(img), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun build(): SpannableStringBuilder {
        return builder
    }
}

class DslTextAttrBuilderImpl : DslTextAttrBuilder{
    var foregroundColorSpan: ForegroundColorSpan? = null
    var fontSizeSpan: AbsoluteSizeSpan? = null
    var fontSizeSpanHeight: Int? = null
    var fontSizeSpanWight: Int? = null
    var onClickSpan: ClickableSpan? = null
    override fun setColor(color: String) {
        foregroundColorSpan = ForegroundColorSpan(Color.parseColor(color))
    }

    override fun setSize(size: Int) {
        fontSizeSpan = AbsoluteSizeSpan(size, true)
    }

    override fun setImageHeight(size: Int) {
        fontSizeSpanHeight = size
    }

    override fun setImageWight(size: Int) {
        fontSizeSpanWight = size
    }

    override fun onClick(useUnderLine: Boolean, onClick: (View) -> Unit) {
        onClickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick(widget)
            }
        }
    }

}
