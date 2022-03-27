package com.easy.lib_ui.dialog.image

import com.easy.lib_ui.R
import com.easy.lib_util.ext.getColor
import com.luck.picture.lib.config.SelectMimeType

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/12/8
 *   功能描述:  图片选择器  参数配置
 */
data class SelectImageConfig(
    var uiConfig: UIConfig = UIConfig(),
    var config : PictureConfig = PictureConfig()
    ) {
    data class UIConfig(
        var selectPhotoText: String = "从相册选择",
        var selectPhotoTextColor: Int = R.color.app_color.getColor(),
        var selectCameraText: String = "打开相机",
        var selectCameraTextColor: Int = R.color.app_color.getColor(),
        var selectCancelText: String = "取消",
        var selectCancelTextColor: Int = R.color.color_999.getColor(),
    )

    data class PictureConfig(
        var chooseType: Int = SelectMimeType.ofImage(),//ofVideo()  ofAll ofAudio
        var maxSelectNumber: Int = 1,//最大选择数量
        var isPreview: Boolean = true,//是否开启预览
        var isCrop: Boolean = true,  //是否开启裁剪  maxSelectNumber>1 则自动为 false
        var isCircRatioWigth :Int = 1, //裁剪宽
        var isCircRatioHeight :Int = 1,//裁剪高
        var isCompress:Boolean = true, //是否开启压缩  默认开启
    )
}