package com.easy.lib_ui.mvvm.viewmodel.flow

import com.easy.lib_util.toast.ToastUtil.toast
import com.google.gson.JsonParseException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.net.SocketTimeoutException

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/04/06
 *   功能描述: 网络请求状态定义
 */
enum class HttpError(var code: Int, var errorMsg: String) {
    TOKEN_EXPIRE(3001, "token is expired"),
    PARAMS_ERROR(4003, "params is error")
    // ...... more
}

internal fun handlingApiExceptions(code: Int?, errorMsg: String?) = when (code) {
    HttpError.TOKEN_EXPIRE.code -> toast(HttpError.TOKEN_EXPIRE.errorMsg)
    HttpError.PARAMS_ERROR.code -> toast(HttpError.PARAMS_ERROR.errorMsg)
    else -> errorMsg?.let { toast(it) }
}

internal fun handlingExceptions(e: Throwable) = when (e) {
    is HttpException -> toast(e.message())

    is CancellationException -> {
    }
    is SocketTimeoutException -> {
    }
    is JsonParseException -> {
    }
    else -> {
    }
}
