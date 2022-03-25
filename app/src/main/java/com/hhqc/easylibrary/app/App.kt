package com.hhqc.easylibrary.app

import android.app.Activity
import androidx.paging.ExperimentalPagingApi
import com.easy.lib_base.constant.BaseApplication

/**
 * 程序入口
 */
@ExperimentalPagingApi
class App : BaseApplication(){
    override val isDebug: Boolean
        get() = true

    override fun initOtherSDK() {

    }

    override fun getInvalidActivity(): Class<out Activity>? {
        return null
    }

    override fun getHttpHost(): String = if (isDebug)"http://192.168.0.99:9966/" else "http://110.42.202.238:9966/"

}