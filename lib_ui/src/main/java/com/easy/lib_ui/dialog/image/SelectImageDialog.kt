package com.easy.lib_ui.dialog.image

import android.os.Bundle
import com.easy.lib_ui.R
import com.easy.lib_ui.databinding.DialogSelectImgBinding
import com.easy.lib_ui.dialog.BindDialogFragment
import com.easy.lib_util.LogUtil
import com.easy.lib_util.ext.singleClick
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.language.LanguageConfig
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
                selectForPhoto()

            }
            R.id.select_camera -> {
               selectForCamera()
            }
        }
    }


    /**
     * 从相册选择
     */
    private fun selectForPhoto(){
        PictureSelector.create(this)
            .openGallery(config.config.chooseType)//选择类型
            .setImageEngine(GlideEngine)//图片加载引擎
            .setLanguage(LanguageConfig.CHINESE) //中文显示
            .setCropEngine(if (config.config.isCrop) ImageCropEngine(
                config.config.isCircRatioHeight,
                config.config.isCircRatioWigth
            ) else null) //设置图片裁剪引擎
            .setCompressEngine(if (config.config.isCompress) ImageCompressEngine() else null) //设置图片压缩引擎
            .setMaxSelectNum(config.config.maxSelectNumber)//图片最大选择数量
            .setMaxVideoSelectNum(config.config.maxSelectNumber) //视频最大选择数量
            .setSelectionMode(if (config.config.maxSelectNumber == 1) SelectModeConfig.SINGLE else SelectModeConfig.MULTIPLE)
            .isPreviewImage(true) //支持预览图片
            .isPreviewVideo(true) //支持预览视频
            .isEmptyResultReturn(true)//支持没选择可返回
            .isWithSelectVideoImage(true) //支持视频图片一同选择
            .isGif(true) //支持gif 图显示
            .isWebp(true)
            .isBmp(true)
            .forResult(object :OnResultCallbackListener<LocalMedia>{
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    val mList: MutableList<String> = ArrayList()
                    result?.forEach {
                        mList.add(it.availablePath)
                    }

                    onResult(mList)
                }

                override fun onCancel() {
                    dismiss()
                }

            })
    }
    /**
     * 打开相机
     */
    private fun selectForCamera(){

        PictureSelector.create(this)
            .openCamera(config.config.chooseType)//选择类型
            .setCropEngine(if (config.config.isCrop) ImageCropEngine(
                config.config.isCircRatioHeight,
                config.config.isCircRatioWigth
            ) else null) //设置图片裁剪引擎
            .setCompressEngine(if (config.config.isCompress) ImageCompressEngine() else null) //设置图片压缩引擎
            .forResult(object :OnResultCallbackListener<LocalMedia>{
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    val mList: MutableList<String> = ArrayList()
                    result?.forEach {
                        mList.add(it.availablePath)
                    }

                    onResult(mList)
                }

                override fun onCancel() {
                    dismiss()
                }

            })
    }

}