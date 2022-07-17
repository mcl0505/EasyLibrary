package com.easy.lib_pay.wechat

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：HttpWxService
 * 创建时间：2020/10/7
 * 功能描述：  微信网络请求
 */
interface HttpWxService {


    /**
     * TODO 获取微信授权信息
     */
    @GET("/sns/oauth2/access_token")
    fun getCook(
        @Query("appid") appid: String,
        @Query("secret") secret: String,
        @Query("code") code: String,
        @Query("grant_type") grant_type: String
    ): Call<WXAccessTokenBean>

    /**
     * TODO 获取微信用户信息
     */
    @GET("/sns/userinfo")
    fun getUserInfo(
        @Query("access_token") access_token: String,
        @Query("openid") openid: String): Call<WXUserInfoBean>
}