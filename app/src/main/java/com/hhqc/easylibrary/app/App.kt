package com.hhqc.easylibrary.app

import android.app.Activity
import com.easy.lib_base.constant.BaseApplication

/**
 * 程序入口
 */
class App : BaseApplication(){
    override val isDebug: Boolean
        get() = true

    override fun initOtherSDK() {

    }

    override fun getInvalidActivity(): Class<out Activity>? {
        return null
    }

    //测试
    override fun getHttpHost(): String = "https://www.wanandroid.com"

}