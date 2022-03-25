package com.easy.lib_ui.view

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.easy.lib_ui.R

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2021/1/4
 * 功能描述：
 */
class RadiusViewDelegate(private val view: View, private val context: Context, attrs: AttributeSet) {
    private val gdBackground = GradientDrawable()
    private var backgroundColor = 0
    private var radius = 0
    private var topLeftRadius = 0
    private var topRightRadius = 0
    private var bottomLeftRadius = 0
    private var bottomRightRadius = 0
    private var strokeWidth = 0
    private var strokeColor = 0
    var radiusHalfHeightEnable = false
        private set
    private var isWidthHeightEqual = false
    private var isRippleEnable = false
    private val radiusArr = FloatArray(8)
    private fun obtainAttributes(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RadiusTextView)
        backgroundColor = ta.getColor(R.styleable.RadiusTextView_rv_backgroundColor, 0)
        radius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_radius, 0)
        strokeWidth = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_strokeWidth, 0)
        strokeColor = ta.getColor(R.styleable.RadiusTextView_rv_strokeColor, 0)
        topLeftRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_topLeftRadius, 0)
        topRightRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_topRightRadius, 0)
        bottomLeftRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_bottomLeftRadius, 0)
        bottomRightRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_bottomRightRadius, 0)
        isRippleEnable = ta.getBoolean(R.styleable.RadiusTextView_rv_rippleEnable, false)
        ta.recycle()
    }

    fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
        setBgSelector()
    }

    fun setRadius(radius: Int) {
        this.radius = dp2px(radius.toFloat())
        setBgSelector()
    }

    fun setStrokeWidth(strokeWidth: Int) {
        this.strokeWidth = dp2px(strokeWidth.toFloat())
        setBgSelector()
    }

    fun setStrokeColor(strokeColor: Int) {
        this.strokeColor = strokeColor
        setBgSelector()
    }

    fun setEadiusHalfHeightEnable(isRadiusHalfHeight: Boolean) {
        radiusHalfHeightEnable = isRadiusHalfHeight
        setBgSelector()
    }

    fun setTopLeftRadius(topLeftRadius: Int) {
        this.topLeftRadius = topLeftRadius
        setBgSelector()
    }

    fun setTopRightRadius(topRightRadius: Int) {
        this.topRightRadius = topRightRadius
        setBgSelector()
    }

    fun setBottomLeftRadius(bottomLeftRadius: Int) {
        this.bottomLeftRadius = bottomLeftRadius
        setBgSelector()
    }

    fun setBottomRightRadius(bottomRightRadius: Int) {
        this.bottomRightRadius = bottomRightRadius
        setBgSelector()
    }

    fun getBackgroundColor(): Int {
        return backgroundColor
    }

    fun getRadius(): Int {
        return radius
    }

    fun getStrokeWidth(): Int {
        return strokeWidth
    }

    fun getStrokeColor(): Int {
        return strokeColor
    }

    var widthHeightEqualEnable: Boolean
        get() = isWidthHeightEqual
        set(isWidthHeightEqual) {
            this.isWidthHeightEqual = isWidthHeightEqual
            setBgSelector()
        }

    fun gettopLeftRadius(): Int {
        return topLeftRadius
    }

    fun gettopRightRadius(): Int {
        return topRightRadius
    }

    fun getbottomLeftRadius(): Int {
        return bottomLeftRadius
    }

    fun getbottomRightRadius(): Int {
        return bottomRightRadius
    }

    protected fun dp2px(dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    protected fun sp2px(sp: Float): Int {
        val scale = context.resources.displayMetrics.scaledDensity
        return (sp * scale + 0.5f).toInt()
    }

    private fun setDrawable(gd: GradientDrawable, color: Int, strokeColor: Int) {
        gd.setColor(color)
        if (topLeftRadius <= 0 && topRightRadius <= 0 && bottomRightRadius <= 0 && bottomLeftRadius <= 0) {
            gd.cornerRadius = radius.toFloat()
        } else {
            radiusArr[0] = topLeftRadius.toFloat()
            radiusArr[1] = topLeftRadius.toFloat()
            radiusArr[2] = topRightRadius.toFloat()
            radiusArr[3] = topRightRadius.toFloat()
            radiusArr[4] = bottomRightRadius.toFloat()
            radiusArr[5] = bottomRightRadius.toFloat()
            radiusArr[6] = bottomLeftRadius.toFloat()
            radiusArr[7] = bottomLeftRadius.toFloat()
            gd.cornerRadii = radiusArr
        }
        gd.setStroke(strokeWidth, strokeColor)
    }

    fun setBgSelector() {
        val bg = StateListDrawable()
        setDrawable(gdBackground, backgroundColor, strokeColor)
        if (Build.VERSION.SDK_INT >= 21 && isRippleEnable && view.isEnabled) {
            val rippleDrawable = RippleDrawable(getColorSelector(backgroundColor, backgroundColor, if (backgroundColor == 2147483647) backgroundColor else backgroundColor), gdBackground, null)
            view.background = rippleDrawable
        } else {
            if (view.isEnabled) {
                bg.addState(intArrayOf(-16842919, -16842913), gdBackground)
            }
            if (Build.VERSION.SDK_INT >= 16) {
                view.background = bg
            } else {
                view.setBackgroundDrawable(bg)
            }
        }
    }

    @TargetApi(11)
    private fun getColorSelector(normalColor: Int, pressedColor: Int, enabledColor: Int): ColorStateList {
        return ColorStateList(arrayOf(intArrayOf(16842908), intArrayOf(16843518), intArrayOf(16842919), intArrayOf(-16842910), IntArray(0)), intArrayOf(pressedColor, pressedColor, pressedColor, enabledColor, normalColor))
    }

    init {
        obtainAttributes(context, attrs)
    }
}