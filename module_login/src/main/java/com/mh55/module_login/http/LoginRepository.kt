package com.mh55.module_login.http

import com.easy.lib_ui.http.BaseResult
import com.easy.lib_ui.http.HttpRequest
import com.easy.lib_ui.mvvm.SingleLiveEvent
import com.easy.lib_ui.mvvm.StateLiveData
import com.easy.lib_ui.mvvm.model.BaseModel

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/04/12
 *   功能描述:
 */
class LoginRepository :  BaseModel()  {
    private val api = HttpRequest.getService(LoginService::class.java)
    suspend fun login(login:StateLiveData<String>){
        executeReqWithFlow({api.login()},login)
    }

}