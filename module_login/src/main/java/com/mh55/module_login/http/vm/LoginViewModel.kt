package com.mh55.module_login.http.vm

import com.easy.lib_ui.mvvm.StateLiveData
import com.easy.lib_ui.mvvm.viewmodel.BaseViewModel
import com.mh55.module_login.http.LoginRepository

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/04/12
 *   功能描述:
 */
class LoginViewModel : BaseViewModel() {
    private val mRepository by lazy { LoginRepository() }
    var mLogin = StateLiveData<String>()
    fun login(){
        launch{mRepository.login("18302696784","123456",mLogin)}
    }
}