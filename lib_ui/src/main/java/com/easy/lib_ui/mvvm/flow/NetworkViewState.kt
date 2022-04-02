package com.easy.lib_ui.mvvm.flow

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/04/02
 *   功能描述: 网络请求常用状态
 */

data class NetworkViewState(
    val content: String = "等待网络请求内容",
    val pageStatus: PageStatus = PageStatus.Success
)

sealed class NetworkViewEvent {
    data class ShowToast(val message: String) : NetworkViewEvent()
    object ShowLoadingDialog : NetworkViewEvent()
    object DismissLoadingDialog : NetworkViewEvent()
}

sealed class NetworkViewAction {
    object PageRequest : NetworkViewAction()
    object PartRequest : NetworkViewAction()
    object MultiRequest : NetworkViewAction()
    object ErrorRequest : NetworkViewAction()
}


sealed class PageStatus {
    /**
     * 加载中
     */
    object Loading : PageStatus()

    /**
     * 加载成功
     */
    object Success : PageStatus()

    /**
     * 加载失败
     */
    data class Error(val throwable: Throwable) : PageStatus()
}