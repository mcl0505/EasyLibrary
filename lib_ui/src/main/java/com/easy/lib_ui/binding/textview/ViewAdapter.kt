package com.easy.lib_ui.binding.textview

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import coil.loadAny
import com.easy.lib_util.app.AppConfig
import com.easy.lib_util.ext.getDrawable
import com.zzhoujay.richtext.RichText


/**
 * 渲染富文本内容
 */
@SuppressLint("CheckResult")
@BindingAdapter(value = ["webText"], requireAll = false)
fun setWebText(
    textView: TextView,
    webText: String = ""
) {
    RichText.from(webText)
        .into(textView)
    textView.movementMethod = ScrollingMovementMethod.getInstance()
}

//Paint. STRIKE_THRU_TEXT_FLAG   中间横线
//Paint. UNDERLINE_TEXT_FLAG  底部横线
@BindingAdapter(value = ["addCenterLine"], requireAll = false)
fun addCenterLine(textView: TextView,isCenter:Boolean){
    //添加中间横线
    textView.paint.flags = if (isCenter) Paint. STRIKE_THRU_TEXT_FLAG else Paint. UNDERLINE_TEXT_FLAG
}