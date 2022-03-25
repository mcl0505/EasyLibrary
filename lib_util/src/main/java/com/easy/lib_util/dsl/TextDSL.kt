package com.easy.lib_util.dsl

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView

/**
 *   公司名称: xxx
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/03/24
 *   功能描述:
 *
 *
 */

//   使用方式
//tvTestDsl.buildSpannableString {
//    addText("我已详细阅读并同意")
//    addText("《隐私政策》"){
//        setColor("#0099FF")
//        onClick(false) {
//            //do some thing
//        }
//    }
//}

interface DslSpannableStringBuilder {
    //增加一段文字
    fun addText(text: String, method: (DslSpanBuilder.() -> Unit)? = null)
}

interface DslSpanBuilder {
    //设置文字颜色
    fun setColor(color: String)
    //设置点击事件
    fun onClick(useUnderLine: Boolean = true, onClick: (View) -> Unit)
}

//为 TextView 创建扩展函数，其参数为接口的扩展函数
fun TextView.buildSpannableString(init: DslSpannableStringBuilder.() -> Unit) {
    //具体实现类
    val spanStringBuilderImpl = DslSpannableStringBuilderImpl()
    spanStringBuilderImpl.init()
    movementMethod = LinkMovementMethod.getInstance()
    //通过实现类返回SpannableStringBuilder
    text = spanStringBuilderImpl.build()
}


class DslSpannableStringBuilderImpl : DslSpannableStringBuilder {
    private val builder = SpannableStringBuilder()
    //记录上次添加文字后最后的索引值
    var lastIndex: Int = 0
    var isClickable = false

    override fun addText(text: String, method: (DslSpanBuilder.() -> Unit)?) {
        val start = lastIndex
        builder.append(text)
        lastIndex += text.length
        val spanBuilder = DslSpanBuilderImpl()
        method?.let { spanBuilder.it() }
        spanBuilder.apply {
            onClickSpan?.let {
                builder.setSpan(it, start, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                isClickable = true
            }
            if (!useUnderLine) {
                val noUnderlineSpan = NoUnderlineSpan()
                builder.setSpan(noUnderlineSpan, start, lastIndex, Spanned.SPAN_MARK_MARK)
            }
            foregroundColorSpan?.let {
                builder.setSpan(it, start, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    fun build(): SpannableStringBuilder {
        return builder
    }
}

class DslSpanBuilderImpl : DslSpanBuilder {
    var foregroundColorSpan: ForegroundColorSpan? = null
    var onClickSpan: ClickableSpan? = null
    var useUnderLine = true

    override fun setColor(color: String) {
        foregroundColorSpan = ForegroundColorSpan(Color.parseColor(color))
    }

    override fun onClick(useUnderLine: Boolean, onClick: (View) -> Unit) {
        onClickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick(widget)
            }
        }
        this.useUnderLine = useUnderLine
    }
}

class NoUnderlineSpan : UnderlineSpan() {
    override fun updateDrawState(ds: TextPaint) {
        ds.color = ds.linkColor
        ds.isUnderlineText = false
    }
}