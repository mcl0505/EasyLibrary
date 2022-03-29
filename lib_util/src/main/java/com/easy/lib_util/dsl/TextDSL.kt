package com.easy.lib_util.dsl

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt

/**
 *   公司名称: xxx
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/03/24
 *   功能描述:
 *
 *
 */

//   使用方式
//tvTestDsl.buildString {
//    addText("我已详细阅读并同意")
//    addText("《隐私政策》"){
//        setColor("#0099FF")
//        onClick(false) {
//            //do some thing
//        }
//    }
//}

//为 TextView 创建扩展函数，其参数为接口的扩展函数
inline fun TextView.renderString(init: DslSpannableStringBuilder.() -> Unit) {
    //具体实现类
    val spanStringBuilderImpl = DslSpannableStringBuilderImpl()
    spanStringBuilderImpl.init()
    movementMethod = LinkMovementMethod.getInstance()
    //通过实现类返回SpannableStringBuilder
    text = spanStringBuilderImpl.build()
}
//操作
interface DslSpannableStringBuilder {
    //增加一段文字
    fun addText(text: CharSequence, method: (DslSpanBuilder.() -> Unit)? = null)
    //增加一张图片
    fun addImg(img: Drawable, method: (DslSpanBuilder.() -> Unit)? = null)
}
//属性
interface DslSpanBuilder {
    //设置文字颜色
    fun setColor(@ColorInt color: Int)
    //设置文字大小
    fun setSize(size: Int)
    //设置点击事件
    fun onClick(useUnderLine: Boolean = false, onClick: (View) -> Unit)
    //设置图片高
    fun setImageHeight(size: Int)
    //设置图片宽
    fun setImageWight(size: Int)
}
//操作是实现类
class DslSpannableStringBuilderImpl : DslSpannableStringBuilder {
    private val builder = SpannableStringBuilder()
    //记录上次添加文字后最后的索引值
    var lastIndex: Int = 0
    var isClickable = false

    override fun addText(text: CharSequence, method: (DslSpanBuilder.() -> Unit)?) {
        val start = lastIndex
        builder.append(text)
        lastIndex += text.length
        val spanBuilder = DslSpanBuilderImpl()
        method?.let { spanBuilder.it() }
        spanBuilder.apply {
            //添加文字点击事件
            onClickSpan?.let {
                builder.setSpan(it, start, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                isClickable = true
            }
            //是否显示下划线
            if (useUnderLine) {
                val noUnderlineSpan = NoUnderlineSpan()
                builder.setSpan(noUnderlineSpan, start, lastIndex, Spanned.SPAN_MARK_MARK)
            }
            //设置文字大小
            fontSizeSpan?.let {
                builder.setSpan(fontSizeSpan,start, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            //设置文字颜色
            foregroundColorSpan?.let {
                builder.setSpan(it, start, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    override fun addImg(img: Drawable, method: (DslSpanBuilder.() -> Unit)?) {
        builder.append("   ")
        val spanBuilder = DslSpanBuilderImpl()
        spanBuilder.apply {
            //设置图片宽高  根据图片大小设置
            if (fontSizeSpanHeight == null && fontSizeSpanWight == null){
                img.setBounds(0, 0, img.intrinsicWidth, img.intrinsicHeight)
            }else {
                img.setBounds(0, 0, spanBuilder.fontSizeSpanWight!!, spanBuilder.fontSizeSpanHeight!!)
            }
            //设置图片点击事件
            onClickSpan?.let {
                builder.setSpan(it, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                isClickable = true
            }
        }
        //将图片添加到字符串中
        builder.setSpan(VerticalImageSpan(img), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    fun build(): SpannableStringBuilder {
        return builder
    }
}
//属性实现类
class DslSpanBuilderImpl : DslSpanBuilder {
    var foregroundColorSpan: ForegroundColorSpan? = null
    var onClickSpan: ClickableSpan? = null
    var useUnderLine = true
    var fontSizeSpan: AbsoluteSizeSpan? = null
    var fontSizeSpanHeight: Int? = null
    var fontSizeSpanWight: Int? = null

    override fun setColor( color: Int) {
        foregroundColorSpan = ForegroundColorSpan(color)
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

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
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
//图片添加
class VerticalImageSpan @JvmOverloads constructor(drawable: Drawable) : ImageSpan(drawable) {
    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fontMetricsInt: Paint.FontMetricsInt?
    ): Int {
        var drawable = getDrawable()
        if (drawable == null) {
            drawable = this.drawable
        }
        val rect = drawable.bounds
        if (fontMetricsInt != null) {
            val fmPaint = paint.fontMetricsInt
            val fontHeight = fmPaint.bottom - fmPaint.top
            val drHeight = rect.bottom - rect.top
            val top = drHeight / 2 - fontHeight / 4
            val bottom = drHeight / 2 + fontHeight / 4
            fontMetricsInt.ascent = -bottom
            fontMetricsInt.top = -bottom
            fontMetricsInt.bottom = top
            fontMetricsInt.descent = top
        }
        return rect.right
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val drawable = getDrawable()
        canvas.save()
        val transY = (bottom - top - drawable.bounds.bottom) / 2 + top
        canvas.translate(x, transY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }
}