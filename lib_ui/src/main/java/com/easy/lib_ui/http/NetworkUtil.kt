package com.easy.lib_ui.http

import android.content.Context
import android.net.ConnectivityManager
import com.easy.lib_util.app.EasyApplication


/**
 * 网络工具类
 */
object NetworkUtil {
    fun isConnected(): Boolean {
        val manager = EasyApplication.instance.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as? ConnectivityManager?
        if (manager != null) {
            val info = manager.activeNetworkInfo
            return info != null && info.isAvailable
        }
        return false
    }

    /**
     * 是否使用WIFI联网
     * @return
     */
    fun isWifiConnection(): Boolean {
        val manager = EasyApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            ?: return false
        val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return if (networkInfo != null) {
            networkInfo.isAvailable && networkInfo.isConnected
        } else false
    }
}