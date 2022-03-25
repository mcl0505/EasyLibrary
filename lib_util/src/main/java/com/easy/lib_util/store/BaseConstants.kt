package com.easy.lib_util.store

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/12/14
 *   功能描述:
 */
object BaseConstants {
    //token
    const val tokenKey = "Authorization"
    var tokenValue: String
        get() = MmkvUtil.getValue(tokenKey, tokenKey)
        set(token) {
            MmkvUtil.putValue(tokenKey, token)
            isLogin = !(token == tokenKey)
        }

    //最后点击的时间
    private const val LAST_CLICK_TIME = "lastClickTime"
    var lastClickTime: Long
        get() = MmkvUtil.getValue(LAST_CLICK_TIME, 0)
        set(token) = MmkvUtil.putValue(LAST_CLICK_TIME, token)

    //是否处于登录状态
    private const val IS_LOGIN = "isLogin"
    var isLogin: Boolean
        get() = MmkvUtil.getValue(IS_LOGIN, false)
        set(value)  {
            MmkvUtil.putValue(IS_LOGIN, value)
        }
    open fun outLogin(){
        tokenValue = tokenKey
        isLogin = false
    }
}
