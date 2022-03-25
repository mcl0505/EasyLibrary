package com.easy.lib_ui.http

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/12/14
 *   功能描述: 
 */
data class BaseResult<T>(
    val msg: String? = "数据获取失败",
    val code: Int = HttpHandler.CODE_HTTP_ERROR,
    var data: T? = null
) : IBaseResponse<T?> {

    override fun code() = code

    override fun msg() = msg

    override fun data() = data

    override fun isSuccess() = code == HttpHandler.CODE_HTTP_SUCCESS
    override fun isInvalid() = code == HttpHandler.CODE_TOKEN_INVALID

}