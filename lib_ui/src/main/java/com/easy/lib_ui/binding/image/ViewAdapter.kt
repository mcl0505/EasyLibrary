package com.easy.lib_ui.binding.image

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.loadAny
import com.easy.lib_util.app.AppConfig
import com.easy.lib_util.ext.getDrawable

/**
 *  url="@{@drawable/cat}"
 *
 */

@SuppressLint("CheckResult")
@BindingAdapter(value = ["url", "placeholderRes", "errorRes"], requireAll = false)
fun setImageUri(
    imageView: ImageView,
    url: Any?,
    placeholder: Drawable? ,
    errorImg: Drawable?
) {
    imageView.loadAny(url){
        if (placeholder == null){
            placeholder(AppConfig.ImageView.placeholderRes.getDrawable())
        }else {
            placeholder(placeholder)
        }

        if (errorImg == null){
            error(AppConfig.ImageView.errorRes.getDrawable())
        }else {
            error(errorImg)
        }

    }
}