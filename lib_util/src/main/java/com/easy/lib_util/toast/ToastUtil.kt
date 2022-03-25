package com.easy.lib_util.toast

import android.widget.Toast
import com.easy.lib_util.app.EasyApplication

/**
 * 吐司工具类
 *  在两秒内如果是相同的消息则只提示一次
 */
object ToastUtil {
    private var time: Long = 0
    private var oldMsg: String? = null
    fun toast(msg:String = ""){
        if (msg != oldMsg) {
            create(msg)
            time = System.currentTimeMillis()
        } else {
            if (System.currentTimeMillis() - time > 2000) {
                create(msg)
                time = System.currentTimeMillis()
            }
        }
        oldMsg = msg
    }


    private fun create(massage: String) {
        Toast.makeText(EasyApplication.getContext(),massage,Toast.LENGTH_SHORT).show()
    }

}


//弹框使用扩展函数
fun String.toast(){
    ToastUtil.toast(this)
}