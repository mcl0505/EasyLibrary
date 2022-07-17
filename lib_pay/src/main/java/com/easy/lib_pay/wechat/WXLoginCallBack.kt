package com.easy.lib_pay.wechat

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：WXLoginCallBack
 * 创建时间：2020/10/7
 * 功能描述： 微信登录回调，获取用户信息成功后走后台登录接口
 */
interface WXLoginCallBack {
    fun loginSuccess(userInfoBean: WXUserInfoBean)
    fun loginError(msg :String)
}