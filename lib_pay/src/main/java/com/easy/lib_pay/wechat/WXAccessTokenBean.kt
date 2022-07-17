package com.easy.lib_pay.wechat

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：WXAccessTokenBean
 * 创建时间：2020/10/7
 * 功能描述：获取微信授权信息
 */
data class WXAccessTokenBean(
    val access_token:String,
    val expires_in:Int,
    val refresh_token:String,
    val openid:String,
    val scope:String,
    val unionid:String
    )