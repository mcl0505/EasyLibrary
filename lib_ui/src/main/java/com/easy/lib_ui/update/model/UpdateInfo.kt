package com.easy.lib_ui.update.model

/**
 * 更新信息
 */
internal data class UpdateInfo(
    // 更新标题
    var updateTitle: CharSequence = "",
    // 更新内容
    var updateContent: CharSequence = "",
    // apk 下载地址
    var apkUrl: String = "",
    // 更新配置
    var config: UpdateConfig = UpdateConfig(),
    // ui配置
    var uiConfig: UiConfig = UiConfig()
)