package com.easy.library.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.easy.lib_ui.R

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2021/4/15
 * 功能描述：带比例的线型布局
 */
class ScaleLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val mScreenHeight: Int
    private val mHeightPercent: Float
    val height2: Int
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height2, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    init {
        val dm = context.resources.displayMetrics
        mScreenHeight = dm.heightPixels
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ScaleLinearLayout)
        mHeightPercent = ta.getFloat(R.styleable.ScaleLinearLayout_mll_height_percent, 0f)
        height2 = (mHeightPercent * mScreenHeight).toInt()
        ta.recycle()
    }
}