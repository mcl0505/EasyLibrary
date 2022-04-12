package com.easy.lib_ui.mvvm

import androidx.lifecycle.MutableLiveData
import com.easy.lib_ui.http.BaseResult

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/04/12
 *   功能描述: 用于将请求状态分发给UI
 */
class StateLiveData <T> : MutableLiveData<BaseResult<T>>() {
}