package com.easy.lib_ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：TitleBar
 * 创建时间：2020/7/3
 * 功能描述： 自定义标题栏
 */
class TitleBar @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(mContext, attrs, defStyleAttr) {
    private var titleCenter: String?
    private val titleRight: String?
    private val titleLeft: String?
    private var titleCenterColor: Int
    private var titleRightColor: Int
    private var titleLeftColor: Int
    private var titleColor: Int
    private var titleRightIcon: Int
    private var titleLeftIcon: Int
    lateinit var tvTitleCenter: TextView
    lateinit var tvTitleRight: TextView
    lateinit var tvTitleLeft: TextView
    lateinit var imgTitleLeft: ImageView
    lateinit var imgTitleRight: ImageView
    lateinit var titleLine:View
    private fun initData() {
        tvTitleCenter.text = titleCenter
        tvTitleRight.text = titleRight
        tvTitleLeft.text = titleLeft
        tvTitleCenter.setTextColor(titleCenterColor)
        tvTitleRight.setTextColor(titleRightColor)
        tvTitleLeft.setTextColor(titleLeftColor)
        imgTitleLeft.setImageResource(titleLeftIcon)
        imgTitleRight.setImageResource(titleRightIcon)
        setBackgroundColor(titleColor)
    }

    private fun initView(
        context: Context
    ) {
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this, true)
        tvTitleCenter = findViewById(R.id.title_bar_center)
        tvTitleRight = findViewById(R.id.title_bar_right_text)
        tvTitleLeft = findViewById(R.id.title_bar_left_text)
        imgTitleLeft = findViewById(R.id.title_bar_left_img)
        imgTitleRight = findViewById(R.id.title_bar_right_img)
        titleLine = findViewById(R.id.titleLine)

    }

    init {
        val ta = mContext.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        titleRightIcon = ta.getResourceId(R.styleable.TitleBar_bar_titie_tv_right_icon, 0)
        titleLeftIcon = ta.getResourceId(R.styleable.TitleBar_bar_titie_tv_left_icon, 0)
        titleCenter = ta.getString(R.styleable.TitleBar_bar_title_tv_center)
        titleLeft = ta.getString(R.styleable.TitleBar_bar_titie_tv_left)
        titleRight = ta.getString(R.styleable.TitleBar_bar_titie_tv_right)
        titleCenterColor = ta.getColor(R.styleable.TitleBar_bar_title_tv_center_color, -0x1)
        titleRightColor = ta.getColor(R.styleable.TitleBar_bar_title_tv_right_color, -0x1)
        titleLeftColor = ta.getColor(R.styleable.TitleBar_bar_title_tv_left_color, -0x1)
        titleColor = ta.getColor(R.styleable.TitleBar_bar_title_color, -0x000000)
        initView(mContext)
        initData()
        ta.recycle()
    }
}