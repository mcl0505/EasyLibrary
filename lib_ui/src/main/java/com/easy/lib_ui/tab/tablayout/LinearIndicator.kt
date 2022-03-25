package com.easy.lib_ui.tab.tablayout

import android.graphics.Paint
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.view.Gravity
import com.easy.lib_util.ext.toPx
import com.google.android.material.tabs.TabLayout

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/10/26
 *   功能描述:线性指示器
 */
open class LinearIndicator : BaseIndicator() {

    private var angle: Int = 0


    fun setAngle(angle: Int): LinearIndicator {
        this.angle = angle
        return this
    }

    override fun bind() {
        tabLayout?.post {
            val drawable = ShapeDrawable()
            if (height == MATCH)
                height = tabLayout?.height!!
            if (angle <= 0f)
                angle = if (height == 0) 100 else height / 2
            val f_angle = angle.toFloat()
            val outerR =
                floatArrayOf(f_angle, f_angle, f_angle, f_angle, f_angle, f_angle, f_angle, f_angle)
            val shape = RoundRectShape(outerR, null, null)
            drawable.shape = shape
            drawable.paint.style = Paint.Style.FILL

            val layerDrawable = LayerDrawable(arrayOf(drawable))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layerDrawable.setLayerHeight(0, height)
                layerDrawable.setLayerWidth(0, width)
                layerDrawable.setLayerGravity(0, Gravity.CENTER)
            }


            if (width == 0 && height == 0)
                tabLayout?.setSelectedTabIndicator(drawable)
            else
                tabLayout?.setSelectedTabIndicator(layerDrawable)

            if (height == 0)
                tabLayout?.setSelectedTabIndicatorHeight(3.toPx())
            else
                tabLayout?.setSelectedTabIndicatorHeight(height)

            //对自适应宽度进行处理
            if (width <= 0 && tabLayout?.tabSelectedIndicator is LayerDrawable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    (tabLayout?.tabSelectedIndicator as LayerDrawable).setLayerWidth(
                        0,
                        tabLayout?.getTabAt(0)!!.view.width
                    )
                }
                tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        tab?.apply {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                (tabLayout?.tabSelectedIndicator as LayerDrawable).setLayerWidth(
                                    0,
                                    tab.view.width
                                )
                            }
                        }
                    }

                })
            }
        }
    }
}