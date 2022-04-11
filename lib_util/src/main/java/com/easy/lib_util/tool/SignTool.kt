package com.easy.lib_util.tool

import android.annotation.SuppressLint
import android.content.Context
import com.easy.lib_util.LogUtil
import com.easy.lib_util.app.AppUtil
import okhttp3.internal.and
import java.lang.Exception
import java.lang.StringBuilder
import java.security.MessageDigest

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/11/26
 *   功能描述:  代码方式获取 32位MD5签名(字母小写  不含 : 号)
 *   使用Android Studio 获取签名:(字母大写  有 : 号)
 *   Terminal ==> keytool -list -v -keystore fanzairecovery.jks
 *                输入签名密码
 */
object SignTool {
    //调用示例
    //SignTool.printSignatureMD5(CHAuthService.this,"com.sccngitv.dvb");
    /**
     * App 基础信息
     */
    fun printSignatureMD5(
        mContext: Context,
        packageName: String = AppUtil.getPackageName(),
    ) {
        LogUtil.d("${AppUtil.getApplicationName(mContext)}    启动成功 " +
                "\nAPP包名       => ${packageName} " +
                "\nAPP版本名称    => ${AppUtil.getAppVersionName(mContext)} " +
                "\nAPP版本号      => ${AppUtil.getAppVersionCode(mContext)} " +
                "\n32位 MD5 签名  => ${getMD5MessageDigest(mContext, packageName)}")
    }

    @SuppressLint("WrongConstant")
    private fun getMD5MessageDigest(mContext: Context, str: String?): String {
        return try {
            var i = 0
            val signature = mContext.packageManager.getPackageInfo(str!!, 64).signatures[0]
            val instance = MessageDigest.getInstance("md5")
            instance.update(signature.toByteArray())
            val digest = instance.digest()
            val stringBuilder = StringBuilder()
            val length = digest.size
            while (i < length) {
                var toHexString = Integer.toHexString(digest[i] and 255)
                if (toHexString.length == 1) {
                    val stringBuilder2 = StringBuilder()
                    stringBuilder2.append("0")
                    stringBuilder2.append(toHexString)
                    toHexString = stringBuilder2.toString()
                }
                stringBuilder.append(toHexString)
                i++
            }
            stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "null"
        }
    }
}