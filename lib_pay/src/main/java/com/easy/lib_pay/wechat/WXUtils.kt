package com.easy.lib_pay.wechat

import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.easy.lib_pay.*
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.modelbiz.WXOpenCustomerServiceChat
import com.tencent.mm.opensdk.modelpay.PayReq
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import android.graphics.Bitmap

import android.graphics.BitmapFactory
import android.util.Log
import com.easy.lib_pay.qq.*
import com.tencent.mm.opensdk.modelmsg.*
import java.io.ByteArrayOutputStream


/**
 * 公司名称：~漫漫人生路~总得错几步~
 * 创建作者：Android 孟从伦
 * 创建时间：2022/5/9
 * 功能描述： 微信工具
 *  wxPay==>微信支付
 *  wxLogin==>微信登录
 *  getWxInfo==>获取用户信息
 *  openService==>打开微信客服界面
 *  shareText==> 分享文本
 *  shareImage==> 分享图片
 *  shareMusic==> 分享音乐
 *  shareVideo==> 分享视频
 *  shareWeb==> 分享网址
 *  sendApplet==> 分享小程序
 */

object WXUtils {

    /**
     * 微信支付
     *
     * @param info 支付信息
     */
    fun wxPay(info: String?=null) {

        if (info.isNullOrEmpty()){
            Toast.makeText(mContext, "请传入微信支付参数", Toast.LENGTH_SHORT).show()
            return
        }

        mIWXAPI?.let {
            val resultBean = JSON.parseObject(info, WXResultBean::class.java)
            val request = PayReq()
            request.appId = resultBean.appid
            request.partnerId = resultBean.partnerid
            request.prepayId = resultBean.prepayid
            request.packageValue = resultBean.packageValue
            request.nonceStr = resultBean.noncestr
            request.timeStamp = resultBean.timestamp
            request.sign = resultBean.sign
            it.sendReq(request)
        }
    }

    /**
     * 登录微信请求    调起微信授权界面
     */
    fun wxLogin() {
        mIWXAPI?.let {
            val req = SendAuth.Req()
            req.scope = EasyPayConstants.WX_SCOPE
            req.state = EasyPayConstants.WX_STATE
            it.sendReq(req)
        }
    }

    /**
     * 获取微信登录信息
     */
    fun getWxInfo(code:String,loginCallBack: WXLoginCallBack){

        val retrofit = Retrofit.Builder()
            .baseUrl(EasyPayConstants.WX_HOST)
            .build()
        val httpWxService = retrofit.create(HttpWxService::class.java)
        val wxAccessTokenBeanCall: Call<WXAccessTokenBean> = httpWxService.getCook(mWXAppId!!, mWXAppSecret!!, code, "authorization_code")
        wxAccessTokenBeanCall.enqueue(object : Callback<WXAccessTokenBean> {
            override fun onResponse(call: Call<WXAccessTokenBean>, response: Response<WXAccessTokenBean>) {
                val tokenBean = response.body()
                getUserInfo(tokenBean!!.access_token, tokenBean.openid,loginCallBack)
            }

            override fun onFailure(call: Call<WXAccessTokenBean>, t: Throwable) {
                loginCallBack.loginError(t.toString())
            }
        })
    }

    /**
     * 获取微信个人信息
     */
    private fun getUserInfo(access_token: String, openid: String,loginCallBack: WXLoginCallBack) {
        val retrofit = Retrofit.Builder()
            .baseUrl(EasyPayConstants.WX_HOST)
            .build()
        val httpWxService = retrofit.create(HttpWxService::class.java)
        val wxUserInfoBeanCall: Call<WXUserInfoBean> =
            httpWxService.getUserInfo(access_token, openid)
        wxUserInfoBeanCall.enqueue(object : Callback<WXUserInfoBean> {
            override fun onResponse(call: Call<WXUserInfoBean>, response: Response<WXUserInfoBean>) {
                val infoBean = response.body()
                loginCallBack.loginSuccess(infoBean!!)
            }

            override fun onFailure(call: Call<WXUserInfoBean?>?, t: Throwable?) {
                loginCallBack.loginError(t.toString())
            }
        })
    }

    /**
     * 打开微信客服
     * @param corpId 企业id
     * @param url 客服地址
     */
    fun openService(corpId: String,url:String) {
        mIWXAPI?.let {
            if (it.wxAppSupportAPI >= Build.SUPPORT_OPEN_CUSTOMER_SERVICE_CHAT) {

                val req = WXOpenCustomerServiceChat.Req()
                req.corpId = corpId // 企业ID
                req.url = url // 客服URL
                it.sendReq(req)
            } else {
                Toast.makeText(mContext, "当前版本不支持打开微信客服", Toast.LENGTH_SHORT).show()
            }
        }

    }

    /**
     * 分享文本
     * @param text 内容文本
     * @param scene 路劲类型
     */
    fun shareText(text:String,scene:Int = EasyPayConstants.WX_SESSION){
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        val textObj = WXTextObject()
        textObj.text = text
        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = textObj
        msg.description = text
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("text")
        req.message = msg
        req.scene = scene
        //调用api接口，发送数据到微信
        mIWXAPI?.sendReq(req)
    }

    /**
     * 分享图片
     * @param mBitmap 图片
     * @param scene 路劲类型
     */
    fun shareImage(mBitmap: Bitmap,scene:Int = EasyPayConstants.WX_SESSION){
        //初始化 WXImageObject 和 WXMediaMessage 对象
        val imgObj = WXImageObject(mBitmap)
        val msg = WXMediaMessage()
        msg.mediaObject = imgObj
        //设置缩略图
        val thumbBmp = Bitmap.createScaledBitmap(mBitmap, 150, 150, true)
        mBitmap.recycle()
        msg.thumbData = bmpToByteArray(thumbBmp, true)
        //构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = scene
//        req.userOpenId = getOpenId()
        //调用api接口，发送数据到微信
        mIWXAPI!!.sendReq(req)
    }

    /**
     * 分享音乐
     * @param musicUrl 音乐地址
     * @param musicTitle 音乐标题
     * @param musicDes 音乐描述
     * @param musicBitmap 音乐缩略图
     * @param scene 路劲类型
     */
    fun shareMusic(musicUrl:String,musicTitle:String,musicDes:String,musicBitmap:Bitmap,scene:Int = EasyPayConstants.WX_SESSION){
        //初始化一个WXMusicObject，填写url
        val  music = WXMusicObject()
        music.musicUrl= musicUrl
        //用 WXMusicObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = music
        msg.title = musicTitle
        msg.description = musicDes
        //设置音乐缩略图
        msg.thumbData = bmpToByteArray(musicBitmap, true)
        //构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("music")
        req.message = msg
        req.scene = scene
//        req.userOpenId = getOpenId()
        //调用api接口，发送数据到微信
        mIWXAPI!!.sendReq(req)
    }

    /**
     * 分享视频
     * @param videoUrl 视频地址
     * @param videoTitle 视频标题
     * @param videoDes 视频描述
     * @param videoBitmap 视频缩略图
     * @param scene 路劲类型
     */
    fun shareVideo(videoUrl:String,videoTitle:String,videoDes:String,videoBitmap:Bitmap,scene:Int = EasyPayConstants.WX_SESSION){
        //初始化一个WXMusicObject，填写url
        val  video = WXVideoObject()
        video.videoUrl = videoUrl
        //用 WXMusicObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = video
        msg.title = videoTitle
        msg.description = videoDes
        //设置视频缩略图
        msg.thumbData = bmpToByteArray(videoBitmap, true)
        //构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("video")
        req.message = msg
        req.scene = scene
//        req.userOpenId = getOpenId()
        //调用api接口，发送数据到微信
        mIWXAPI!!.sendReq(req)
    }

    /**
     * 分享网站
     * @param webUrl 网站地址
     * @param webTitle 网站标题
     * @param webDes 网站描述
     * @param webBitmap 网站缩略图
     * @param scene 路劲类型
     */
    fun shareWeb(webUrl:String,webTitle:String,webDes:String,webBitmap:Bitmap,scene:Int = EasyPayConstants.WX_SESSION){
        //初始化一个WXMusicObject，填写url
        val  video = WXWebpageObject()
        video.webpageUrl  = webUrl
        //用 WXMusicObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = video
        msg.title = webTitle
        msg.description = webDes
        //设置视频缩略图
        msg.thumbData = bmpToByteArray(webBitmap, true)
        //构造一个Req
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("webpage")
        req.message = msg
        req.scene = scene
//        req.userOpenId = getOpenId()
        //调用api接口，发送数据到微信
        mIWXAPI!!.sendReq(req)
    }

    /**
     * 分享小程序
     * @param title 标题
     * @param description 描述
     * @param appletId 小程序原始id
     * @param path 小程序页面路径
     * @param mBitmap 展示的缩略图
     * @param scene 路劲类型
     */
    fun shareApplet(title: String, description: String, appletId:String,path: String, mBitmap: Bitmap,scene:Int = EasyPayConstants.WX_SESSION) {
        val miniProgramObj = WXMiniProgramObject()
        miniProgramObj.webpageUrl = "http://www.qq.com" // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW // 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = appletId // 小程序原始id
        miniProgramObj.path = path //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
        val msg = WXMediaMessage(miniProgramObj)
        msg.title = title //标题  会显示出来
        msg.description = description //好像没有在界面中展示
        msg.thumbData = bmpToByteArray(mBitmap, true)
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("miniProgram")
        req.message = msg
        req.scene = scene
        mIWXAPI!!.sendReq(req)
    }

    private fun buildTransaction(type: String?): String? {
        return if (type == null) System.currentTimeMillis()
            .toString() else type + System.currentTimeMillis()
    }

    private fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray? {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bmp.recycle()
        }
        val result: ByteArray = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

}

inline fun Any.shareWX(init: WXShareBuilder.()->Unit){
    val builder = WXShareBuilderImpl()
    builder.init()
}
interface WXShareBuilder{
    fun shareImage(params:(WXShareParamsBuilder.()->Unit))
    fun shareText(params:(WXShareParamsBuilder.()->Unit))
    fun shareMusic(params:(WXShareParamsBuilder.()->Unit))
    fun shareVideo(params:(WXShareParamsBuilder.()->Unit))
    fun shareWeb(params:(WXShareParamsBuilder.()->Unit))
    fun shareApplet(params:(WXShareParamsBuilder.()->Unit))
}
class WXShareBuilderImpl : WXShareBuilder{
    override fun shareImage(params: WXShareParamsBuilder.() -> Unit) {
        val paramsBuilder = WXShareParamsBuilderImpl()
        params?.let { paramsBuilder.it() }
        WXUtils.shareImage(paramsBuilder.mBitmap,paramsBuilder.mScene)
    }

    override fun shareText(params: WXShareParamsBuilder.() -> Unit) {
        val paramsBuilder = WXShareParamsBuilderImpl()
        params?.let { paramsBuilder.it() }
        WXUtils.shareText(paramsBuilder.mTitle,paramsBuilder.mScene)
    }

    override fun shareMusic(params: WXShareParamsBuilder.() -> Unit) {
        val paramsBuilder = WXShareParamsBuilderImpl()
        params?.let { paramsBuilder.it() }
        WXUtils.shareMusic(
            paramsBuilder.mMusicUrl,
            paramsBuilder.mTitle,
            paramsBuilder.mDescription,
            paramsBuilder.mBitmap,
            paramsBuilder.mScene)
    }

    override fun shareVideo(params: WXShareParamsBuilder.() -> Unit) {
        val paramsBuilder = WXShareParamsBuilderImpl()
        params?.let { paramsBuilder.it() }
        WXUtils.shareVideo(
            paramsBuilder.mVideoUrl,
            paramsBuilder.mTitle,
            paramsBuilder.mDescription,
            paramsBuilder.mBitmap,
            paramsBuilder.mScene)
    }

    override fun shareWeb(params: WXShareParamsBuilder.() -> Unit) {
        val paramsBuilder = WXShareParamsBuilderImpl()
        params?.let { paramsBuilder.it() }
        WXUtils.shareWeb(
            paramsBuilder.mVideoUrl,
            paramsBuilder.mTitle,
            paramsBuilder.mDescription,
            paramsBuilder.mBitmap,
            paramsBuilder.mScene)
    }

    override fun shareApplet(params: WXShareParamsBuilder.() -> Unit) {
        val paramsBuilder = WXShareParamsBuilderImpl()
        params?.let { paramsBuilder.it() }
        WXUtils.shareApplet(
            paramsBuilder.mTitle,
            paramsBuilder.mDescription,
            paramsBuilder.mAppletAppId,
            paramsBuilder.mAppletPath,
            paramsBuilder.mBitmap,
            paramsBuilder.mScene)
    }

}

interface WXShareParamsBuilder{
    /**
     * 添加分享的方式
     * @param scene 分享方式
     * EasyPayConstants.WX_SESSION ==> 好友
     * EasyPayConstants.WX_TIMELINE ==> 朋友圈
     * EasyPayConstants.WX_FAVORITE ==> 收藏
     */
    fun addScene(scene:Int = EasyPayConstants.WX_FAVORITE)

    /**
     * 图片bitmap
     * @param bitmap 缩略图
     */
    fun addImageBitmap(bitmap:Bitmap)

    /**
     * 添加标题
     */
    fun addTitle(text:String)

    /**
     * 添加描述
     */
    fun addDescription(description:String)

    /**
     * 添加音乐地址  仅在分享音乐时生效
     */
    fun addMusicUrl(musicUrl:String)

    /**
     * 添加视频地址  仅在分享视频时生效
     */
    fun addVideoUrl(videoUrl:String)

    /**
     * 分享网址  仅在分享网址时生效
     */
    fun addWebUrl(webUrl:String)

    /**
     * 添加小程序信息
     * @param appId 小程序原始id
     * @param path 打开小程序跳转的路劲
     */
    fun addApplet(appId:String,path:String)
}
class WXShareParamsBuilderImpl : WXShareParamsBuilder{
    var mScene:Int = EasyPayConstants.WX_SESSION
    lateinit var mBitmap:Bitmap
    var mTitle:String = ""
    var mDescription:String = ""
    var mMusicUrl:String = ""
    var mVideoUrl:String = ""
    var mWebUrl:String = ""
    var mAppletAppId:String = ""
    var mAppletPath:String = ""
    override fun addScene(scene: Int) {
        mScene = scene
    }

    override fun addImageBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
    }

    override fun addTitle(text: String) {
        mTitle = text
    }

    override fun addDescription(description: String) {
        mDescription = description
    }

    override fun addMusicUrl(musicUrl: String) {
        mMusicUrl = musicUrl
    }

    override fun addVideoUrl(videoUrl: String) {
        mVideoUrl = videoUrl
    }

    override fun addWebUrl(webUrl: String) {
        mWebUrl = webUrl
    }

    override fun addApplet(appId: String, path: String) {
        mAppletAppId = appId
        mAppletPath = path
    }
}