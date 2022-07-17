package com.easy.lib_pay.wechat

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：WXUserInfoBean
 * 创建时间：2020/10/7
 * 功能描述：  微信用户信息
 */
data class WXUserInfoBean(
    var openid: String? = null,//微信标识
    var nickname: String? = null,//微信用户昵称
    var sex : Int = 0,
    var province: String? = null,
    var city: String? = null,
    var country: String? = null,
    var headimgurl: String? = null,//微信用户头像
    var unionid: String? = null,
    var privilege: List<String>? = null
)