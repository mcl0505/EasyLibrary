package com.easy.lib_ui.mvvm

import androidx.lifecycle.MutableLiveData

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/12/28
 *   功能描述: 登录失效专属 LiveData 请勿在其他场景使用
 */
object TokenInvalidLiveData : SingleLiveEvent<Boolean>() {
}