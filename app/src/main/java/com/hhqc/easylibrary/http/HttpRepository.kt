package com.hhqc.easylibrary.http

import com.easy.lib_ui.http.HttpRequest
import com.easy.lib_ui.mvvm.model.BaseModel


/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/1/15
 *   功能描述: 网络请求
 */
class HttpRepository : BaseModel() {
    private var api = HttpRequest.getService(ApiService::class.java)

}