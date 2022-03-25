package com.easy.lib_util.ext

import android.os.SystemClock
import android.view.View
import androidx.databinding.BindingAdapter
import com.easy.lib_util.R
import com.easy.lib_util.store.BaseConstants

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2020/10/24
 * 功能描述：
 */

private var lastClickTime: Long = 0
//防止快速点击造成打开多个界面   只允许在 1秒内只能点击一次  single(2000){}   可自定义时间
/**
 * 防止快速点击造成打开多个界面
 */
fun <T : View> T.singleClick(time: Int = 500, block: (T) -> Unit) {
    this.setOnClickListener {
        val curClickTime = SystemClock.uptimeMillis()
        val lastClickTime = (it.getTag(R.id.singleClickId) as? Long) ?:0
        if (curClickTime - lastClickTime >= time) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            it.setTag(R.id.singleClickId,lastClickTime)
            block(it as T)
            BaseConstants.lastClickTime = lastClickTime
        }
    }
}
@BindingAdapter(value = ["singleClick"], requireAll = false)
fun viewClick( view: View,block: View.OnClickListener){
    view.singleClick { block.onClick(view) }
}



/**
 * view 显示隐藏
 */
fun View.visibleOrGone(show: Boolean) {
    if (show) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

