package com.easy.lib_pay.wechat


/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：WXResultBean
 * 创建时间：2020/7/17
 * 功能描述：  微信支付回调数据
 */

/**
 * appid : wx2f5332ffbb643be4
 * partnerid : 1502151531
 * prepayid : wx1717381566267048ff7e2e831713876800
 * package : Sign=WXPay
 * noncestr : czZbipVryNyhX3CLcEHVMCrOv1zs0KhC
 * timestamp : 1594978687
 * sign : 37076327A59112F7BD812893DF116039
 */
data class WXResultBean(
    val appid: String = "",
    val partnerid: String = "",
    val prepayid: String = "",
    val packageValue: String = "",
    val noncestr: String = "",
    val timestamp: String = "",
    val sign: String = ""
)