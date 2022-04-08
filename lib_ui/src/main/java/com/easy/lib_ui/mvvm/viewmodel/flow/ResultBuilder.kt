package com.easy.lib_ui.mvvm.viewmodel.flow

import com.easy.lib_util.toast.ToastUtil.toast
import com.easy.lib_util.toast.toast

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/04/06
 *   功能描述: 返回回调
 */
fun <T> ApiResponse<T>.parseData(listenerBuilder: ResultBuilder<T>.() -> Unit) {
    val listener = ResultBuilder<T>().also(listenerBuilder)
    when (this) {
        is ApiSuccessResponse -> listener.onSuccess(this.response)
        is ApiEmptyResponse -> listener.onDataEmpty()
        is ApiFailedResponse -> listener.onFailed(this.errorCode, this.errorMsg)
        is ApiErrorResponse -> listener.onError(this.throwable)
    }
    listener.onComplete()
}

class ResultBuilder<T> {
    var onSuccess: (data: T?) -> Unit = {}
    var onDataEmpty: () -> Unit = {}
    var onFailed: (errorCode: Int?, errorMsg: String?) -> Unit = { _, errorMsg ->
        errorMsg?.let { it.toast() }
    }
    var onError: (e: Throwable) -> Unit = { e ->
        e.message?.let { toast(it) }
    }
    var onComplete: () -> Unit = {}
}