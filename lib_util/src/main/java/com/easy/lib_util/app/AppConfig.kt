package com.easy.lib_util.app

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.easy.lib_util.R

/**
 * App配置
 */
object AppConfig {


    /**
     * 是否需要管理 Activity 堆栈
     */
    var gIsNeedActivityManager = true


    //图片相关配置
    object ImageView {
        //是否使用Glide 加载图片
        var imgLoad:ImageLoad = ImageLoad.GLIDE
        //加载失败显示占位图
        var errorRes: Int = R.drawable.ic_android
        //加载之前占位图
        var placeholderRes: Int = R.drawable.ic_loading
    }

    object StartAndFinish {

        /**
         * ViewModel 是否可以调用 finish 和 startActivity 方法
         */
        var gIsViewModelNeedStartAndFinish = true

        /**
         * ViewModel 是否可以调用 startActivityForResult 方法
         */
        var gIsViewModelNeedStartForResult = true
    }

    //点击事件
    object Click {

        /**
         * 在 xml 配置点击事件，可配置的属性如下：
         * onClickCommand 点击事件
         * isInterval 是否开启防止点击过快
         * intervalMilliseconds 防止点击过快的间隔时间，毫秒为单位
         *
         * 这里可全局设置是否开启防止点击事件过快的功能，局部可单独开启或关闭。
         *
         * 如果关闭，那么和 setOnClickListener 没啥区别
         */
        var gIsClickInterval = false

        /**
         * 点击事件时间间隔
         */
        var gClickIntervalMilliseconds = 800
    }

    enum class ImageLoad{
        GLIDE,
        COIL
    }

}