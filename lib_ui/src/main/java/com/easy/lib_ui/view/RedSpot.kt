package com.easy.lib_ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.easy.lib_ui.R
import com.easy.lib_ui.view.RedSpot

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/11/4
 *   功能描述:  未读消息数量提示控件
 *
 */
class RedSpot : View {
    private var rectf: RectF? = null
    private var rect: Rect? = null
    private var textSize = 25 // 文字大小
    private var contentText: String? = "" // 文字内容
    private var textColor = Color.WHITE // 文字颜色
    private var rs_backgroundColor: Int = Color.RED // 背景颜色
    private var paint: Paint? = null

    constructor(context: Context?) : this(context, null) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RedSpot)
        textSize = typedArray.getInteger(R.styleable.RedSpot_rs_textSize, 25)
        textColor = typedArray.getColor(R.styleable.RedSpot_rs_textColor, Color.WHITE)
        contentText = typedArray.getString(R.styleable.RedSpot_rs_text)
        rs_backgroundColor = typedArray.getColor(R.styleable.RedSpot_rs_backgroundColor, Color.RED)
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        init()
    }

    // 初始化变量
    private fun init() {
        paint = Paint()
        // 设置画笔为抗锯齿
        paint!!.isAntiAlias = true
        // 默认画笔颜色为红色
        paint!!.color = Color.RED
        // 设置绘制模式为填充
        paint!!.style = Paint.Style.FILL
        rect = Rect()
        rectf = RectF()
        paint!!.textSize = textSize.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 画圆
        paint!!.isAntiAlias = true
        paint!!.color = rs_backgroundColor
        val r = if (measuredWidth > measuredHeight) measuredWidth else measuredHeight
        rectf!![paddingLeft.toFloat(), paddingTop.toFloat(), (r - paddingRight).toFloat()] =
            (r - paddingBottom).toFloat()
        canvas.drawArc(rectf!!, 0f, 360f, false, paint!!)

        // 画文字
        paint!!.isAntiAlias = true
        paint!!.color = textColor
        paint!!.textSize = textSize.toFloat()
        paint!!.getTextBounds(contentText, 0, contentText!!.length, rect)
        canvas.drawText(contentText!!,
            (width / 2 - rect!!.width() / 2).toFloat(),
            (height / 2 + rect!!.height() / 2).toFloat(),
            paint!!)
    }

    fun setText(text: Int): RedSpot {
        contentText = text.toString()
        return this
    }

    fun setText(text: String?): RedSpot {
        contentText = text
        return this
    }

    fun setTextColor(textColor: String?): RedSpot {
        this.textColor = Color.parseColor(textColor)
        return this
    }

    fun setBackColor(backgroundColor: String?): RedSpot {
        this.rs_backgroundColor = Color.parseColor(backgroundColor)
        return this
    }

    fun setTextSize(textSize: Int): RedSpot {
        this.textSize = textSize
        return this
    }
}