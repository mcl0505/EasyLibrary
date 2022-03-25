package com.easy.lib_util.image

import android.widget.ImageView
import coil.loadAny
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.easy.lib_util.R
import com.easy.lib_util.app.AppConfig
import com.easy.lib_util.app.EasyApplication

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/01/17
 *   功能描述: 图片加载库使用
 */
object ImageLoader {
    fun loadCoil(mImageView:ImageView,url:Any){
        mImageView.loadAny(url) {
            placeholder(AppConfig.ImageView.placeholderRes) //加载中占位图
            error(AppConfig.ImageView.errorRes) //加载错误占位图
            crossfade(true) // //渐进进出
            scale(Scale.FILL)
        }
    }

    fun loadGlide(mImageView:ImageView,url:Any){
        Glide.with(EasyApplication.instance)
            .load(url)
            .centerCrop()
            .placeholder(AppConfig.ImageView.placeholderRes)
            .error(AppConfig.ImageView.errorRes)
            .diskCacheStrategy(
                DiskCacheStrategy.DATA
            ).into(mImageView)
    }
}

inline fun ImageView.loadImage(url: Any){
    when(AppConfig.ImageView.imgLoad){
        AppConfig.ImageLoad.GLIDE->{
            ImageLoader.loadGlide(this,url)
        }
        AppConfig.ImageLoad.COIL->{
            ImageLoader.loadCoil(this,url)
        }
    }
}
