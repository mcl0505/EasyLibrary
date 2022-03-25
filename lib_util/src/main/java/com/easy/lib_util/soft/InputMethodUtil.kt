package com.easy.lib_util.soft

import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.easy.lib_util.app.EasyApplication

/**
 * 公司：     
 * 作者：Android 孟从伦
 * 创建时间：2021/3/26
 * 功能描述：
 */
object InputMethodUtil {
    /**
     * 关闭指定EditText的输入法
     *
     * @param editText 指定的EditText
     */
    fun closeInputMethod(editText: EditText) {
        val binder = editText.windowToken
        val methodManager = EasyApplication.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
        ) as? InputMethodManager
        methodManager?.hideSoftInputFromWindow(binder, 0)
    }

    fun closeInputMethod(binder: IBinder) {
        val methodManager = EasyApplication.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
        ) as? InputMethodManager
        methodManager?.hideSoftInputFromWindow(binder, 0)
    }

    /**
     * 弹出输入法对话框，并且焦点在指定的EditText上
     *
     * @param editText 指定的EditText
     */
    fun showInputMethod(editText: EditText) {
        // 弹出键盘
        editText.postDelayed({
            val methodManager = EasyApplication.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE
            ) as? InputMethodManager
            methodManager?.showSoftInput(editText, 0)
        }, 200)
    }

    fun focusable(editText: EditText) {
        editText.postDelayed({
            editText.isFocusable = true
            editText.isFocusableInTouchMode = true
            editText.requestFocus()
        }, 200)
    }
}