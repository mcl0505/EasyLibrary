package com.easy.lib_ui.mvvm.viewmodel

import java.util.*

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/04/02
 *   功能描述: 通用的 Ui 改变变量
 */
class UiChangeLiveData {

    //操作等待框
    var showLoadingDialogEvent: String? = null
    var dismissLoadingDialogEvent: String? = null

    init {
        showLoadingDialogEvent = UUID.randomUUID().toString()
        dismissLoadingDialogEvent = UUID.randomUUID().toString()
    }

}