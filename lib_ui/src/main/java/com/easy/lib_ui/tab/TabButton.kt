package com.easy.lib_ui.tab

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.easy.lib_ui.R

/**
 * 公司：
 * 作者：Android 孟从伦
 * 创建时间：2020/12/19
 * 功能描述：
 */
class TabButton @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(mContext, attrs, defStyleAttr) {
    private val mScale: Float
    private val mSelectedIcon: Int
    private val mUnSelectedIcon: Int
    private val mTip: String?
    private val mIconSize: Int
    private val mTextSize: Int
    private val mTextColor: Int
    private val mTextSelectColor: Int
    private var mChecked: Boolean
    private var mImg: ImageView? = null
    private var mText: TextView? = null
    override fun onFinishInflate() {
        super.onFinishInflate()
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        mImg = ImageView(mContext)
        mText = TextView(mContext)
        val params1 = LayoutParams(mIconSize, mIconSize)
        params1.setMargins(0, dp2px(5), 0, 0)
        mImg!!.layoutParams = params1
        val params2 = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params2.setMargins(0, dp2px(1), 0, 0)
        mText!!.layoutParams = params2
        mText!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
        mText!!.text = mTip
        if (mChecked) {
            mImg!!.setImageResource(mSelectedIcon)
            mText!!.setTextColor(mTextSelectColor)
        } else {
            mImg!!.setImageResource(mUnSelectedIcon)
            mText!!.setTextColor(mTextColor)
        }
        addView(mImg)
        addView(mText)
    }

    fun setChecked(checked: Boolean) {
        mChecked = checked
        if (checked) {
            mImg!!.setImageResource(mSelectedIcon)
            mText!!.setTextColor(mTextSelectColor)
        } else {
            mImg!!.setImageResource(mUnSelectedIcon)
            mText!!.setTextColor(mTextColor)
        }
    }

    private fun dp2px(dpVal: Int): Int {
        return (mScale * dpVal + 0.5f).toInt()
    }

    init {
        mScale = mContext.resources.displayMetrics.density
        val ta = mContext.obtainStyledAttributes(attrs, R.styleable.TabButton)
        mSelectedIcon = ta.getResourceId(R.styleable.TabButton_tbn_selected_icon, 0)
        mUnSelectedIcon = ta.getResourceId(R.styleable.TabButton_tbn_unselected_icon, 0)
        mTip = ta.getString(R.styleable.TabButton_tbn_tip)
        mIconSize = ta.getDimension(R.styleable.TabButton_tbn_icon_size, 0f).toInt()
        mTextSize = ta.getDimension(R.styleable.TabButton_tbn_text_size, 0f).toInt()
        mTextColor = ta.getColor(R.styleable.TabButton_tbn_text_color, -0x1000000)
        mTextSelectColor =
            ta.getColor(R.styleable.TabButton_tbn_text_select_color, -0x1000000)
        mChecked = ta.getBoolean(R.styleable.TabButton_tbn_checked, false)
        ta.recycle()
    }
}