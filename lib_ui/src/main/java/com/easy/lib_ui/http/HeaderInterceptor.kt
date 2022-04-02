package com.easy.lib_ui.http

import androidx.collection.ArrayMap
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

/**
 * 无网络状态下智能读取缓存的拦截器
 */
class HeaderInterceptor(val mDefaultHeader: ArrayMap<String, String>) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request().newBuilder()


        val builder = chain.proceed(request.build())
        return builder
    }
}