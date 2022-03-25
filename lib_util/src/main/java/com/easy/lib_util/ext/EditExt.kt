package com.easy.lib_util.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2021/3/22
 * 功能描述：输入框复制
 */

fun EditText.setEditContent(text:String){
    this.text = Editable.Factory.getInstance().newEditable(text)
}

fun EditText.getEditContent():String{
    var content = ""
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            content = p0?.toString()!!
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    })

    return content
}