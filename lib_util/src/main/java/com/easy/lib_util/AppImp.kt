package com.easy.lib_util

import android.app.Activity

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/12/28
 *   功能描述:
 */
interface AppImp {

    /**
     * 初始化其他第三方信息  非必须立即处理的初始化
     */
    fun initOtherSDK()

    /**
     * 获取登录失效Activity
     */
    fun getInvalidActivity():Class<out Activity>?

    /**
     * 获取网络请求接口地址
     */
    fun getHttpHost():String
}