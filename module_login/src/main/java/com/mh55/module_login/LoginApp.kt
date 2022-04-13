package com.mh55.module_login

import android.app.Activity
import com.easy.lib_base.constant.BaseApplication

class LoginApp : BaseApplication() {
    override fun initOtherSDK() {

    }

    override fun getInvalidActivity(): Class<out Activity>? {
        TODO("Not yet implemented")
    }

    override fun getHttpHost(): String = "https://www.wanandroid.com"
}