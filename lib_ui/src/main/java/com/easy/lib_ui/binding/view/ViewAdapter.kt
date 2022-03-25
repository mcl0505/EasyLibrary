package com.easy.lib_ui.binding.view

import android.view.View
import androidx.databinding.BindingAdapter
import com.easy.lib_util.LogUtil
import com.easy.lib_util.app.AppConfig
import com.easy.lib_util.store.BaseConstants


/**
 * 使用方式
 * 在布局文件中声明ViewModel
 * 一:
 * 在控件上添加    onClickCommand="@{viewModel.clickListener}"
 *
 *  val clickListener = View.OnClickListener{
        when(it.id){
            R.id.send->{
                LogUtil.d("点击打印日志  发送")
            }
            R.id.get->{
                LogUtil.d("点击打印日志  获取")
            }
        }
    }
二:
在控件上添加   onClickCommand="@{()->viewModel.logInfo()}"
    fun logInfo(){
        LogUtil.d("点击打印日志")
    }
 */

//@BindingAdapter(
//    value = ["onClickCommand", "isInterval", "intervalMilliseconds"],
//    requireAll = false
//)
//fun onClickCommand(
//    view: View,
//    clickCommand: View.OnClickListener?,
//    isInterval: Boolean = true, //是否开启防止快速点击
//    intervalMilliseconds: Int = 800
//) {
//
//    BaseConstants.lastClickTime = System.currentTimeMillis()
//
//    if (isInterval) {
//        clickCommand?.let { view.clickWithTrigger(intervalMilliseconds.toLong(), it) }
//    } else {
//        view.setOnClickListener(clickCommand)
//    }
//}
