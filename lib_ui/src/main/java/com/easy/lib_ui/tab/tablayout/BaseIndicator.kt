package com.easy.lib_ui.tab.tablayout

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.google.android.material.tabs.TabLayout

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/10/26
 *   功能描述:  设置指示器样式
 */
abstract class BaseIndicator {
    companion object {
        //填充满
        const val MATCH = -1
    }

    protected var width: Int = 0
    protected var height: Int = 0
    protected var context: Context? = null
    protected var tabLayout: TabLayout? = null

    fun bindTabLayout(tabLayout: TabLayout) {
        this.tabLayout = tabLayout
        this.context = tabLayout.context


    }

    fun setColor(@ColorInt color: Int): BaseIndicator {
        tabLayout?.setSelectedTabIndicatorColor(color)
        return this
    }

    fun setWidth(@Px width: Int): BaseIndicator {
        this.width = width
        return this
    }

    fun setHeight(@Px height: Int): BaseIndicator {
        this.height = height
        return this
    }

    fun setGravity(gravity: Int): BaseIndicator {
        tabLayout?.setSelectedTabIndicatorGravity(gravity)
        return this
    }

    abstract fun bind()
}