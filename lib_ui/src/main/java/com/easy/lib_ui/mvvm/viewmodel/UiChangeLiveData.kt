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
    //界面跳转
    var startActivityEvent: String? = null
    var startActivityWithMapEvent: String? = null
    var startActivityEventWithBundle: String? = null
    //界面回调
    var startActivityForResultEvent: String? = null
    var startActivityForResultEventWithMap: String? = null
    var startActivityForResultEventWithBundle: String? = null
    //界面结束与设置回调
    var finishEvent: String? = null
    var setResultEvent: String? = null

    init {
        showLoadingDialogEvent = UUID.randomUUID().toString()
        dismissLoadingDialogEvent = UUID.randomUUID().toString()
        startActivityForResultEvent = UUID.randomUUID().toString()
        startActivityForResultEventWithMap = UUID.randomUUID().toString()
        startActivityForResultEventWithBundle = UUID.randomUUID().toString()

        startActivityEvent = UUID.randomUUID().toString()
        startActivityWithMapEvent = UUID.randomUUID().toString()
        startActivityEventWithBundle = UUID.randomUUID().toString()
        finishEvent = UUID.randomUUID().toString()
        setResultEvent = UUID.randomUUID().toString()
    }

}