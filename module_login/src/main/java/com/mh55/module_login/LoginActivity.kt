package com.mh55.module_login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.easy.lib_base.constant.ARouterPath.PATH_LOGIN_ACTIVITY

@Route(path = PATH_LOGIN_ACTIVITY)
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}