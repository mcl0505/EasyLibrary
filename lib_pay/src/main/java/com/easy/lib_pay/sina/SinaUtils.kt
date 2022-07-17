package com.easy.lib_pay.sina

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.easy.lib_pay.mContext
import com.easy.lib_pay.mWBAPI
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.common.UiError

/**
 * 公司名称：~漫漫人生路~总得错几步~
 * 创建作者：Android 孟从伦
 * 创建时间：2022/5/9
 * 功能描述： 新浪微博
 */

object SinaUtils {

    /**
     * 登录
     * @param mActivity 当前Activity
     * @param info 授权成功回调
     * 需要在当前Activity 中设置回调  不设置将接收不到回调
     *  mWBAPI.authorizeCallback(requestCode, resultCode, data);
     */
    fun sinaLogin(mActivity:AppCompatActivity,info:(mOauth2AccessToken:Oauth2AccessToken)->Unit){
        mWBAPI?.let {
            it.authorize(mActivity,object : WbAuthListener{
                override fun onComplete(p0: Oauth2AccessToken?) {
                    p0?.let {
                        info.invoke(p0)
                    }
                }

                override fun onError(p0: UiError?) {
                    Toast.makeText(mContext, p0?.errorMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {

                }

            })
        }
    }

}