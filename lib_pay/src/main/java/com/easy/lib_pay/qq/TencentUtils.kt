package com.easy.lib_pay.qq

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.easy.lib_pay.mContext
import com.easy.lib_pay.mTencent
import com.tencent.connect.UserInfo
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzoneShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError


/**
 * 公司名称：~漫漫人生路~总得错几步~
 * 创建作者：Android 孟从伦
 * 创建时间：2022/5/9
 * 功能描述：QQ 工具
 */

object TencentUtils {
    /**
     * 登录
     * @param mActivity 当前Activity
     * @param onSuccess 登录成功
     */
    fun tencentLogin(mActivity: Activity,onSuccess:(msg:Any?)->Unit){
        if (mTencent !=null && mTencent?.isSessionValid!!){
            mTencent?.login(mActivity,"all",object : IUiListener{
                override fun onComplete(p0: Any?) {
                    onSuccess.invoke(p0)
                }

                override fun onError(p0: UiError?) {

                }

                override fun onCancel() {

                }

                override fun onWarning(p0: Int) {

                }

            })
        }
    }

    /**
     * 获取用户信息
     * @param onSuccess 获取成功回调
     *
     * {
    "is_yellow_year_vip": "0",
    "ret": 0,
    "figureurl_qq_1": "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/40",
    "figureurl_qq_2": "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
    "nickname": "小罗",
    "yellow_vip_level": "0",
    "msg": "",
    "figureurl_1": "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/50",
    "vip": "0",
    "level": "0",
    "figureurl_2": "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
    "is_yellow_vip": "0",
    "gender": "男",
    "figureurl": "http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/30"
    }
     */
    fun getTencentUserInfo(onSuccess:(msg:Any?)->Unit){
        mTencent?.let {
            val userInfo = UserInfo(mContext,it.qqToken)
            userInfo.getUserInfo(object : IUiListener{
                override fun onComplete(p0: Any?) {
                    p0?.let(onSuccess)
                }

                override fun onError(p0: UiError?) {

                }

                override fun onCancel() {

                }

                override fun onWarning(p0: Int) {

                }

            })
        }
    }

    /**
     * 获取用户id
     * @param onSuccess 获取成功回调
     */
    fun getTencentOpenId(onSuccess:(msg:Any?)->Unit){
        mTencent?.let {
            val userInfo = UserInfo(mContext,it.qqToken)
            userInfo.getOpenId(object : IUiListener{
                override fun onComplete(p0: Any?) {
                    p0?.let(onSuccess)
                }

                override fun onError(p0: UiError?) {

                }

                override fun onCancel() {

                }

                override fun onWarning(p0: Int) {

                }

            })
        }
    }

}

inline fun Activity.shareTencent(init:TencentShareBuilder.()->Unit){
    val builder = TencentShareBuilderImpl(this)
    builder.init()
}
interface TencentShareBuilder{
    /**
     * 分享到QQ好友
     */
    fun shareQQ(params:(TencentShareParamsBuilder.()->Unit),onSuccess: (msg: Any?) -> Unit = {},other:()->Unit = {})

    /**
     * 分享到QQ 空间
     */
    fun shareQQZone(params:(TencentShareParamsBuilder.()->Unit),onSuccess: (msg: Any?) -> Unit = {},other:()->Unit = {})
}

class TencentShareBuilderImpl(val mActivity: Activity) : TencentShareBuilder{
    @RequiresApi(Build.VERSION_CODES.M)
    override fun shareQQ(
        params: TencentShareParamsBuilder.() -> Unit,
        onSuccess: (msg: Any?) -> Unit,
        other: () -> Unit
    ) {
        val paramsBuilder = TencentShareParamsBuilderImpl()
        params?.let { paramsBuilder.it() }
        val bundle = Bundle()
        if (paramsBuilder.mShareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE){
            with(bundle) {
                putString(QQShare.SHARE_TO_QQ_TARGET_URL,paramsBuilder.mTarget)
                putString(QQShare.SHARE_TO_QQ_SUMMARY,paramsBuilder.mSummary)

                if (paramsBuilder.mImageUrl.size>0){
                    putString(QQShare.SHARE_TO_QQ_IMAGE_URL,paramsBuilder.mImageUrl[0])
                }

            }
        }else {
            with(bundle) {
                if(paramsBuilder.mImageUrl.size>0){
                    putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,paramsBuilder.mImageUrl[0])
                }

            }
        }

        with(bundle) {
            putString(QQShare.SHARE_TO_QQ_TITLE,paramsBuilder.mTitle)
            if (paramsBuilder.mAppName.isNotEmpty()){
                putString(QQShare.SHARE_TO_QQ_APP_NAME,paramsBuilder.mAppName)
            }

            putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,paramsBuilder.mShareType)
            putInt(QQShare.SHARE_TO_QQ_EXT_INT,paramsBuilder.mmExtarFlag)
        }

        if (paramsBuilder.mShareType != QQShare.SHARE_TO_QQ_TYPE_AUDIO){
            with(bundle) {
                putString(QQShare.SHARE_TO_QQ_AUDIO_URL, paramsBuilder.mAudioUrl)
            }
        }

        if (paramsBuilder.mShareType != QQShare.SHARE_TO_QQ_MINI_PROGRAM){
            val bundle2 = Bundle()
            with(bundle2) {
                putInt(QQShare.SHARE_TO_QQ_EXT_INT,paramsBuilder.mmExtarFlag)
                putString(QQShare.SHARE_TO_QQ_TITLE,paramsBuilder.mTitle)
                putString(QQShare.SHARE_TO_QQ_TARGET_URL,paramsBuilder.mTarget)
                if (paramsBuilder.mSummary.isNotEmpty()){
                    putString(QQShare.SHARE_TO_QQ_SUMMARY,paramsBuilder.mSummary)
                }

                if (paramsBuilder.mImageUrl.size>0){
                    putString(QQShare.SHARE_TO_QQ_IMAGE_URL,paramsBuilder.mImageUrl[0])
                }

                putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_APPID,paramsBuilder.mMiniProgramAppId)
                putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_PATH,paramsBuilder.mMiniProgramPath)
                putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_TYPE,paramsBuilder.mMiniProgramType)
                putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,paramsBuilder.mShareType)
            }
            if (Looper.getMainLooper().isCurrentThread){
                mTencent?.shareToQQ(mActivity,bundle2,object : IUiListener{
                    override fun onComplete(p0: Any?) {
                        p0?.let(onSuccess)
                    }

                    override fun onError(p0: UiError?) {
                        other.invoke()
                    }

                    override fun onCancel() {
                        other.invoke()
                    }

                    override fun onWarning(p0: Int) {
                        other.invoke()
                    }

                })
            }else {
                Toast.makeText(mContext, "请在主线程中分享", Toast.LENGTH_SHORT).show()
            }
            return
        }

        if (Looper.getMainLooper().isCurrentThread){
            mTencent?.shareToQQ(mActivity,bundle,object : IUiListener{
                override fun onComplete(p0: Any?) {
                    p0?.let(onSuccess)
                }

                override fun onError(p0: UiError?) {
                    other.invoke()
                }

                override fun onCancel() {
                    other.invoke()
                }

                override fun onWarning(p0: Int) {
                    other.invoke()
                }

            })
        }else {
            Toast.makeText(mContext, "请在主线程中分享", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun shareQQZone(
        params: TencentShareParamsBuilder.() -> Unit,
        onSuccess: (msg: Any?) -> Unit,
        other: () -> Unit
    ) {
        val paramsBuilder = TencentShareParamsBuilderImpl()
        params?.let { paramsBuilder.it() }
        val bundle = Bundle()
        with(bundle) {
            putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,paramsBuilder.mShareType)
            putString(QzoneShare.SHARE_TO_QQ_SUMMARY,paramsBuilder.mSummary)
        }

        if (paramsBuilder.mTarget.isNotEmpty()){
            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,paramsBuilder.mTarget)
        }

        if (paramsBuilder.mTitle.isNotEmpty()){
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE,paramsBuilder.mTitle)
        }

        if (paramsBuilder.mShareType != QzoneShare.SHARE_TO_QZONE_TYPE_MINI_PROGRAM){
            val miniProgramBundle = Bundle()
            with(miniProgramBundle) {
                putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,paramsBuilder.mShareType)
                putString(QQShare.SHARE_TO_QQ_TITLE,paramsBuilder.mTitle)
                putString(QQShare.SHARE_TO_QQ_TARGET_URL,paramsBuilder.mTarget)
                putString(QQShare.SHARE_TO_QQ_SUMMARY,paramsBuilder.mSummary)
                putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_APPID,paramsBuilder.mMiniProgramAppId)
                putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_PATH,paramsBuilder.mMiniProgramPath)
                putString(QQShare.SHARE_TO_QQ_MINI_PROGRAM_TYPE,paramsBuilder.mMiniProgramType)
                putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,paramsBuilder.mImageUrl)
            }

            if (Looper.getMainLooper().isCurrentThread){
                mTencent?.shareToQzone(mActivity,miniProgramBundle,object : IUiListener{
                    override fun onComplete(p0: Any?) {
                        p0?.let(onSuccess)
                    }

                    override fun onError(p0: UiError?) {
                        other.invoke()
                    }

                    override fun onCancel() {
                        other.invoke()
                    }

                    override fun onWarning(p0: Int) {
                        other.invoke()
                    }

                })
            }else {
                Toast.makeText(mContext, "请在主线程中分享", Toast.LENGTH_SHORT).show()
            }


            return
        }

        if (Looper.getMainLooper().isCurrentThread){
            mTencent?.shareToQzone(mActivity,bundle,object : IUiListener{
                override fun onComplete(p0: Any?) {
                    p0?.let(onSuccess)
                }

                override fun onError(p0: UiError?) {
                    other.invoke()
                }

                override fun onCancel() {
                    other.invoke()
                }

                override fun onWarning(p0: Int) {
                    other.invoke()
                }

            })
        }else {
            Toast.makeText(mContext, "请在主线程中分享", Toast.LENGTH_SHORT).show()
        }
    }

}

interface TencentShareParamsBuilder{
    /**
     * 分享类型  必填
     */
    fun addShareType(shareType:Int)

    /**
     * 分享的标题 必填
     */
    fun addTitle(charSequence: CharSequence)

    /**
     * 手Q 顶部替换返回按钮文案  选填
     */
    fun addAppName(charSequence: CharSequence)

    /**
     * 图片地址 选填
     */
    fun addImageUrl(imgList: MutableList<String>)

    /**
     * 分享音乐
     */
    fun addAudioUrl(charSequence: CharSequence)

    /**
     * 分享摘要  选填
     */
    fun addSummary(charSequence: CharSequence)

    /**
     * 分享之后点击跳转的url  图文消息->必填
     */
    fun addTarget(charSequence: CharSequence)
    fun addExtarFlag(extarFlag: Int)
    fun addMiniProgramAppId(charSequence: CharSequence)
    fun addMiniProgramPath(charSequence: CharSequence)

    /**
     * 小程序类型  3=正式版  1=体验版
     */
    fun addMiniProgramType(charSequence: CharSequence)
}

class TencentShareParamsBuilderImpl : TencentShareParamsBuilder{
    var mShareType:Int  = QQShare.SHARE_TO_QQ_TYPE_IMAGE
    var mTitle:String = ""
    var mAppName:String = ""
    var mImageUrl:ArrayList<String> = ArrayList()
    var mAudioUrl:String = ""
    var mSummary:String = "返回"
    var mTarget:String = ""
    var mmExtarFlag:Int = QQShare. SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN
    var mMiniProgramAppId:String = ""
    var mMiniProgramPath:String = ""
    var mMiniProgramType:String = "1"
    override fun addShareType(shareType: Int) {
        mShareType = shareType
    }

    override fun addTitle(charSequence: CharSequence) {
        mTitle = charSequence.toString()
    }

    override fun addAppName(charSequence: CharSequence) {
        mAppName = charSequence.toString()
    }

    override fun addImageUrl(imgList: MutableList<String>) {
        mImageUrl.clear()
        mImageUrl.addAll(imgList)
    }

    override fun addAudioUrl(charSequence: CharSequence) {
        mAudioUrl = charSequence.toString()
    }

    override fun addSummary(charSequence: CharSequence) {
        mSummary = charSequence.toString()
    }

    override fun addTarget(charSequence: CharSequence) {
        mTarget = charSequence.toString()
    }

    override fun addExtarFlag(extarFlag: Int) {
        mmExtarFlag = extarFlag
    }

    override fun addMiniProgramAppId(charSequence: CharSequence) {
        mMiniProgramAppId = charSequence.toString()
    }

    override fun addMiniProgramPath(charSequence: CharSequence) {
        mMiniProgramPath = charSequence.toString()
    }

    override fun addMiniProgramType(charSequence: CharSequence) {
        mMiniProgramType = charSequence.toString()
    }

}