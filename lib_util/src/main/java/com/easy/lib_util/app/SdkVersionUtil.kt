package com.easy.lib_util.app

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * 公司：
 * 作者：Android 孟从伦
 * 创建时间：2021/4/1
 * 功能描述：版本相关
 */
object SdkVersionUtil {

    /**
     * hasForyo
     * Android 2.2
     * @return true false
     */
    fun hasFroyo(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
    }

    /**
     * hasGingerbread
     * Android 2.3
     * @return true false
     */
    fun hasGingerbread(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
    }

    /**
     * hasHoneycomb
     *
     * @return true false
     */
    fun hasHoneycomb(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
    }

    /**
     * hasHoneycombMR1
     *
     * @return true false
     */
    fun hasHoneycombMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
    }

    /**
     * hasHoneycombMR2
     *
     * @return true false
     */
    fun hasHoneycombMR2(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2
    }

    /**
     * hasIceCreamSandwich
     *
     * @return true false
     */
    fun hasIceCreamSandwich(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
    }

    /**
     * 4.2以上
     *
     * @return true false
     */
    fun sdk4(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN
    }

    /**
     * Android 6.0 以上
     */
    fun sdk6(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.M
    }

    /**
     * Android 10.0 以上
     */
    fun sdk10(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.Q
    }

    fun getAppVersion(context: Context): Int {
        var version = 0
        try {
            version = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version
    }
}