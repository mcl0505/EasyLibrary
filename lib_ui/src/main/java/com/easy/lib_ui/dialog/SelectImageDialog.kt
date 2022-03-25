package com.easy.lib_ui.dialog

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import com.easy.lib_ui.R
import com.easy.lib_ui.databinding.DialogSelectImgBinding
import com.easy.lib_util.ext.getColor
import com.easy.lib_util.ext.singleClick
import com.kckj.baselibrary.utils.image.GlideEngine
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.style.PictureCropParameterStyle
import com.luck.picture.lib.style.PictureParameterStyle
import com.luck.picture.lib.style.PictureWindowAnimationStyle
import com.luck.picture.lib.tools.PictureFileUtils
import org.jetbrains.anko.textColor
import java.util.ArrayList

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/12/8
 *   功能描述: 图片选择弹框
 */
class SelectImageDialog(
    val config: SelectImageConfig = SelectImageConfig(),
    val onResult: (mList: MutableList<String>) -> Unit,
) :
    BindDialogFragment<DialogSelectImgBinding>(R.layout.dialog_select_img) {
    override fun main(savedInstanceState: Bundle?) {
        super.main(savedInstanceState)

        mDialogBinding.apply {
            selectPicture.apply {
                text = config.uiConfig.selectPhotoText
                textColor = config.uiConfig.selectPhotoTextColor
                singleClick {
                    open(it.id)
                }
            }

            selectCamera.apply {
                text = config.uiConfig.selectCameraText
                textColor = config.uiConfig.selectCameraTextColor
                singleClick {
                    open(it.id)
                }
            }

            selectCancle.apply {
                text = config.uiConfig.selectCancelText
                textColor = config.uiConfig.selectCancelTextColor
                singleClick {
                    this@SelectImageDialog.dismiss()
                }
            }
        }

    }

    private fun open(id: Int) {
        dismiss()
        when (id) {
            R.id.select_picture -> {
                PictureSelector.create(this)
                    .openGallery(config.config.chooseType.type)
                    .loadImageEngine(GlideEngine.createGlideEngine())//加载器
                    .maxSelectNum(config.config.maxSelectNumber)
                    .maxVideoSelectNum(config.config.maxSelectNumber)
                    .selectionMode(if (config.config.maxSelectNumber > 1) PictureConfig.MULTIPLE else PictureConfig.SINGLE)
                    .previewImage(config.config.isPreview)
                    .previewVideo(config.config.isPreview)
                    .enableCrop(config.config.isCrop)
                    .compress(config.config.isCompress)
                    .freeStyleCropEnabled(config.config.isCircleCrop)
                    .circleDimmedLayer(config.config.isCircleCrop)
                    .showCropGrid(config.config.isCropFrame)
                    .showCropFrame(config.config.isCropFrame)//是否显示裁剪边框  圆形裁剪建议去除
                    .synOrAsy(config.config.synOrAsy)
                    .withAspectRatio(config.config.isCircRatioWigth,
                        config.config.isCircRatioHeight)
                    .setPictureWindowAnimationStyle(PictureWindowAnimationStyle().also {
                        it.ofAllAnimation(R.anim.dialog_bottom_in, R.anim.dialog_bottom_out)
                    }) // 自定义相册启动退出动画
                    .setPictureStyle(
                        PictureParameterStyle().also {
// 是否改变状态栏字体颜色(黑白切换)
                            // 是否改变状态栏字体颜色(黑白切换)
                            it.isChangeStatusBarFontColor = false
                            // 是否开启右下角已完成(0/9)风格
                            it.isOpenCompletedNumStyle = false
                            // 是否开启类似QQ相册带数字选择风格
                            it.isOpenCheckNumStyle = false
                            // 相册状态栏背景色
                            it.pictureStatusBarColor = Color.parseColor("#393a3e")
                            // 相册列表标题栏背景色
                            it.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e")
                            // 相册列表标题栏右侧上拉箭头
                            it.pictureTitleUpResId = R.drawable.picture_icon_arrow_up
                            // 相册列表标题栏右侧下拉箭头
                            it.pictureTitleDownResId = R.drawable.picture_icon_arrow_down
                            // 相册文件夹列表选中圆点
                            it.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval
                            // 相册返回箭头
                            it.pictureLeftBackIcon =
                                R.drawable.picture_icon_back
                            // 标题栏字体颜色
                            it.pictureTitleTextColor = R.color.picture_color_white.getColor()
                            // 相册右侧取消按钮字体颜色
                            it.pictureCancelTextColor = R.color.picture_color_white.getColor()
                            // 相册列表勾选图片样式
                            it.pictureCheckedStyle = R.drawable.picture_checkbox_selector
                            // 选择相册目录背景样式
//        mPictureParameterStyle.pictureAlbumStyle = R.drawable.picture_new_item_select_bg;
                            // 相册列表底部背景色
                            // 选择相册目录背景样式
//        mPictureParameterStyle.pictureAlbumStyle = R.drawable.picture_new_item_select_bg;
                            // 相册列表底部背景色
                            it.pictureBottomBgColor =  R.color.picture_color_fa.getColor()
                            // 已选数量圆点背景样式
                            it.pictureCheckNumBgStyle =
                                R.drawable.picture_num_oval
                            // 相册列表底下预览文字色值(预览按钮可点击时的色值)
                            it.picturePreviewTextColor =  R.color.picture_color_fa632d.getColor()
                            // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
                            it.pictureUnPreviewTextColor = R.color.picture_color_9b.getColor()
                            // 相册列表已完成色值(已完成 可点击色值)
                            it.pictureCompleteTextColor = R.color.picture_color_fa632d.getColor()
                            // 相册列表未完成色值(请选择 不可点击色值)
                            it.pictureUnCompleteTextColor = R.color.picture_color_9b.getColor()
                            // 预览界面底部背景色
                            it.picturePreviewBottomBgColor = R.color.picture_color_grey_3e.getColor()
                            // 外部预览界面删除按钮样式
                            it.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete
                            // 外部预览界面是否显示删除按钮
                            it.pictureExternalPreviewGonePreviewDelete = true
                            // 相册右侧按钮背景样式,只针对isWeChatStyle 为true时有效果
                            it.pictureUnCompleteBackgroundStyle = R.drawable.picture_send_button_default_bg
                            // 相册右侧按钮可点击背景样式,只针对isWeChatStyle 为true时有效果
                            it.pictureCompleteBackgroundStyle = R.drawable.picture_send_button_bg
                            // 以下设置如果用不上则不要设置，使用默认即可
                            // 自定义相册右侧文本内容设置
                            it.pictureRightDefaultText = ""
                            // 自定义相册未完成文本内容
                            it.pictureUnCompleteText = ""
                            // 完成文案是否采用(%1$d/%2$d)的字符串，只允许两个占位符哟
                            it.isCompleteReplaceNum = true
                            // 自定义相册完成文本内容
                            it.pictureCompleteText = ""
                            // 自定义相册列表不可预览文字
                            it.pictureUnPreviewText = ""
                            // 自定义相册列表预览文字
                            it.picturePreviewText = ""
                            // 自定义相册标题字体大小
                            it.pictureTitleTextSize = 18
                            // 自定义相册右侧文字大小
                            it.pictureRightTextSize = 14
                            // 自定义相册预览文字大小
                            it.picturePreviewTextSize = 14
                            // 自定义相册完成文字大小
                            it.pictureCompleteTextSize = 14
                            // 自定义原图文字大小
                            it.pictureOriginalTextSize =
                                14
                        }
                    ) // 动态自定义相册主题
                    .setPictureCropStyle(PictureCropParameterStyle(R.color.black.getColor(),R.color.black.getColor(),R.color.white.getColor(),true)) // 动态自定义裁剪主题
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .setLanguage(LanguageConfig.CHINESE)//国际化语言
                    .forResult {
                        val mList: MutableList<String> = ArrayList()
                        it.forEach {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                mList.add(it.androidQToPath)
                            } else {
                                if (it.isCut) { //是否剪切
                                    mList.add(it.cutPath)
                                } else if (it.isCompressed) { //是否压缩
                                    mList.add(it.compressPath)
                                } else {
                                    mList.add(it.path)
                                }
                            }
                        }

                        onResult(mList)

                    }

            }
            R.id.select_camera -> {
                PictureSelector.create(this)
                    .openCamera(config.config.chooseType.type)//
                    .loadImageEngine(GlideEngine.createGlideEngine())//加载器
                    .maxSelectNum(config.config.maxSelectNumber)
                    .maxVideoSelectNum(config.config.maxSelectNumber)
                    .selectionMode(if (config.config.maxSelectNumber > 1) PictureConfig.MULTIPLE else PictureConfig.SINGLE)
                    .previewImage(config.config.isPreview)
                    .previewVideo(config.config.isPreview)
                    .enableCrop(config.config.isCrop)
                    .compress(config.config.isCompress)
                    .freeStyleCropEnabled(config.config.isCircleCrop)
                    .circleDimmedLayer(config.config.isCircleCrop)
                    .showCropGrid(config.config.isCropFrame)
                    .showCropFrame(config.config.isCropFrame)//是否显示裁剪边框  圆形裁剪建议去除
                    .synOrAsy(config.config.synOrAsy)
                    .withAspectRatio(config.config.isCircRatioWigth,
                        config.config.isCircRatioHeight)
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .setLanguage(LanguageConfig.CHINESE)//国际化语言
                    .forResult {
                        val mList: MutableList<String> = ArrayList()
                        it.forEach {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                mList.add(it.androidQToPath)
                            } else {
                                if (it.isCut) { //是否剪切
                                    mList.add(it.cutPath)
                                } else if (it.isCompressed) { //是否压缩
                                    mList.add(it.compressPath)
                                } else {
                                    mList.add(it.path)
                                }
                            }
                        }

                        onResult(mList)

                    }
            }
        }


    }

    /**
     * 清楚缓存的路劲与剪切压缩后的图片
     */
    fun cleanPath() {
        //包括裁剪和压缩后的缓存，要在上传成功后调用，type 指的是图片or视频缓存取决于你设置的ofImage或ofVideo 注意：需要系统sd卡权限
        PictureFileUtils.deleteCacheDirFile(mContext, config.config.chooseType.type)
        // 清除所有缓存 例如：压缩、裁剪、视频、音频所生成的临时文件
        PictureFileUtils.deleteAllCacheDirFile(mContext)
    }
}