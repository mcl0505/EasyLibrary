package com.easy.lib_pay

import android.content.Context
import android.util.Log
import com.sina.weibo.sdk.openapi.IWBAPI
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.sina.weibo.sdk.openapi.WBAPIFactory

import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.openapi.SdkListener
import com.tencent.connect.share.QQShare
import java.lang.Exception
import com.tencent.tauth.Tencent





/**
 * 全局变量
 */
var mIWXAPI: IWXAPI?=null
var mContext: Context?=null
var mWXAppId:String?=null
var mWXAppSecret:String?=null

/**
 * 新浪微博
 */
var mWBAPI: IWBAPI?=null
var mSinaAppKey:String?=null
var mSinaRedirectUrl:String = "http://www.sina.com"
var mSinaCope:String = ("email,direct_messages_read,direct_messages_write,"
        + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
        + "follow_app_official_microblog," + "invitation_write")

/**
 * QQ
 */
var mTencent: Tencent? = null

inline fun Context.initEasyPay(init: EasyPayBuilder.() -> Unit){
    mContext = this
    val builderImpl = EasyPayBuilderImpl()
    builderImpl.init()
}

interface EasyPayBuilder{
    /**
     * 初始化微信
     * @param appId 应用id
     * @param appSecret 应用密钥
     */
    fun initWX(appId:String,appSecret:String)

    /**
     * 初始化支付宝
     */
    fun initAli()

    /**
     * 初始化新浪微博
     * @param appId 应用id
     * @param redirectUrl 与官网设置的回调地址一致
     * @param cope 获取的信息列表
     */
    fun initSina(appId: String,redirectUrl:String,cope:String)

    /**
     * @param appId
     * @param authorities   com.tencent.sample.fileprovider  与xml 中配置的fileprovider一致
     *         <provider
    android:authorities="com.tencent.sample.fileprovider"
    android:name="androidx.core.content.FileProvider"
    android:exported="false"
    android:grantUriPermissions="true"
    >
    <meta-data
    android:name="android.support.FILE_PROVIDER_PATHS"
    android:resource="@xml/file_paths"/>
    </provider>
     */
    fun initQQ(appId: String,authorities:String)
}

class EasyPayBuilderImpl : EasyPayBuilder{
    override fun initWX(appId: String, appSecret: String) {
        mWXAppId = appId
        mWXAppSecret = appSecret
        mIWXAPI = WXAPIFactory.createWXAPI(mContext, appId, false)
        mIWXAPI?.registerApp(appId)
        Log.d("initEasyPay", "==========================  微信 始化成功 ======================")
    }

    override fun initAli() {
        Log.d("initEasyPay", "==========================  支付宝 始化成功 ======================")
    }

    override fun initSina(appId: String, redirectUrl: String, cope: String) {
        mSinaAppKey = appId
        mSinaRedirectUrl = redirectUrl
        mSinaCope = cope
        val authInfo = AuthInfo(mContext, mSinaAppKey, mSinaRedirectUrl, mSinaCope)
        mWBAPI = WBAPIFactory.createWBAPI(mContext)
        mWBAPI?.registerApp(mContext, authInfo,object : SdkListener{
            override fun onInitSuccess() {
                Log.d("initEasyPay", "==========================  新浪微博 初始化成功 ======================")
            }

            override fun onInitFailure(p0: Exception?) {
                Log.d("initEasyPay", "==========================  新浪微博 初始化失败 ======================")
                Log.d("initEasyPay", "失败原因：\n${p0?.message}")
            }

        })
    }

    /**
     * @param appId
     * @param authorities
     */
    override fun initQQ(appId: String, authorities: String) {
        mTencent = Tencent.createInstance(appId, mContext, authorities)
        Log.d("initEasyPay", "==========================  腾讯QQ 初始化成功 ======================")
    }

}


