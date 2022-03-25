package com.easy.lib_ui.update.model

import com.easy.lib_ui.R
import com.easy.lib_util.ext.getColor
import com.easy.lib_util.ext.getString


/**
 * UiConfig UI 配置
 */
data class UiConfig(
    // 自定义UI 布局id
    var customLayoutId: Int? = R.layout.dialog_update_app,
    // 更新弹窗中的logo
    var updateLogoImgRes: Int? = null,
    // 标题相关设置
    var titleTextSize: Float? = null,
    var titleTextColor: Int? = null,
    // 更新内容相关设置
    var contentTextSize: Float? = null,
    var contentTextColor: Int? = null,
    // 更新按钮相关设置
    var updateBtnBgColor: Int? = null,
    var updateBtnBgRes: Int? = null,
    var updateBtnTextColor: Int? = R.color.white.getColor(),
    var updateBtnTextSize: Float? = null,
    var updateBtnText: CharSequence = "立即更新",
    // 取消按钮相关设置
    var cancelBtnBgColor: Int? = null,
    var cancelBtnBgRes: Int? = null,
    var cancelBtnTextColor: Int? = null,
    var cancelBtnTextSize: Float? = null,
    var cancelBtnText: CharSequence = "",

    // 开始下载时的Toast提示文字
    var downloadingToastText: CharSequence = R.string.toast_download_apk.getString(),
    // 下载中 下载按钮以及通知栏标题前缀，进度自动拼接在后面
    var downloadingBtnText: CharSequence = R.string.downloading.getString(),
    // 下载出错时，下载按钮及通知栏标题
    var downloadFailText: CharSequence = R.string.download_fail.getString()
)