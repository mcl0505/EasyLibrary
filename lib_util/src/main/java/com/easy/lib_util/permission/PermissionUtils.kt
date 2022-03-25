package com.easy.lib_util.permission

import android.content.Context
import androidx.fragment.app.FragmentActivity
import android.content.pm.PackageManager

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * 权限请求框架,具体彩参照 郭霖   https://github.com/guolindev/PermissionX
 *
 *
 */
object PermissionUtils {

    fun init(activity: FragmentActivity): PermissionMediator {
        return PermissionMediator(activity)
    }


    fun init(fragment: Fragment): PermissionMediator {
        return PermissionMediator(fragment)
    }

    /**
     * 用于检查权限是否被授予的帮助函数。
     *
     * @param context Any context, will not be retained.
     * @param permission Specific permission name to check. e.g. [android.Manifest.permission.CAMERA].
     * @return True if this permission is granted, False otherwise.
     */
    open fun isGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context,
            permission) == PackageManager.PERMISSION_GRANTED
    }


}