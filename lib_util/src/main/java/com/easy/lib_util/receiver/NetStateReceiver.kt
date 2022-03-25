package com.easy.lib_util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.easy.lib_util.bus.LiveDataBus

/**
 * 公司名称: xxx
 * 创建作者: Android 孟从伦
 * 创建时间: 2022/03/23
 * 功能描述: 网络状态监听广播
 */
class NetStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isAvailable) {
            //说明网络是连接的
            val type = networkInfo.type
            when (type) {
                ConnectivityManager.TYPE_MOBILE -> {
                    LiveDataBus.send("NetStateReceiver",NetState.CONNECT_MOBILE)
//                    LiveDataBus.getInstance().with("NetStateReceiver",NetState::class.java).postValue(NetState.CONNECT_MOBILE)
                }
                ConnectivityManager.TYPE_WIFI -> {
                    LiveDataBus.send("NetStateReceiver",NetState.CONNECT_WIFI)
//                    LiveDataBus.getInstance().with("NetStateReceiver",NetState::class.java).postValue(NetState.CONNECT_WIFI)
                }
            }
        } else {
            LiveDataBus.send("NetStateReceiver",NetState.CONNECT_NO)
//            LiveDataBus.getInstance().with("NetStateReceiver",NetState::class.java).postValue(NetState.CONNECT_NO)
        }
    }

    enum class NetState{
        CONNECT_NO,//网络没有连接
        CONNECT_WIFI,//连接wifi
        CONNECT_MOBILE//连接手机流量
    }
}