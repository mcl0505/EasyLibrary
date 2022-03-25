package com.easy.lib_ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2021/1/4
 * 功能描述：
 */
class RadiusTextView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context!!, attrs, defStyleAttr) {
    val delegate: RadiusViewDelegate
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        delegate.setBgSelector()
    }

    init {
        delegate = RadiusViewDelegate(this, context!!, attrs!!)
    }
}