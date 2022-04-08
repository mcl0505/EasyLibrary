package com.easy.lib_ui.mvvm.viewmodel.flow

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/04/06
 *   功能描述:
 */
typealias StateLiveData<T> = LiveData<ApiResponse<T>>
typealias StateMutableLiveData<T> = MutableLiveData<ApiResponse<T>>

@MainThread
fun <T> StateMutableLiveData<T>.observeState(
    owner: LifecycleOwner,
    listenerBuilder: ResultBuilder<T>.() -> Unit,
) {
    observe(owner) { apiResponse -> apiResponse.parseData(listenerBuilder) }
}

@MainThread
fun <T> LiveData<ApiResponse<T>>.observeState(
    owner: LifecycleOwner,
    listenerBuilder: ResultBuilder<T>.() -> Unit,
) {
    observe(owner) { apiResponse -> apiResponse.parseData(listenerBuilder) }
}