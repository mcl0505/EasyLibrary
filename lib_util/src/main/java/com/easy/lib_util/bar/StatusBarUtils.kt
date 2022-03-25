package com.easy.lib_util.bar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View

/**
 * @Author Create by mcl
 * @Date 2020/5/11
 * @ClassName StatusBarUtils
 * @描述   状态栏工具
 *
 * setStatusBarTransparent   状态栏设置透明
 * setStateBarTextColor     设置状态栏字体颜色  亮色 或者 暗色
 */
object StatusBarUtils {

    /**
     * 状态栏设置透明
     * @param activity
     */
    fun setStatusBarTransparent(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = activity.window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            activity.window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * 设置状态栏字体颜色  亮色 或者 暗色
     * @param activity 当前 Activity
     * @param dark true = 黑色   false = 白色
     */
    fun setStateBarTextColor(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        when(dark){
            true -> decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            false-> decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        var mStatusBarHeight = 0

        if (mStatusBarHeight == 0) {
            val res = context.resources
            val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                mStatusBarHeight = res.getDimensionPixelSize(resourceId)
            }
        }
        return mStatusBarHeight
    }


}