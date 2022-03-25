package com.easy.lib_util.permission

import android.Manifest
import android.app.Activity
import android.graphics.Color
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.easy.lib_util.permission.dialog.RationaleDialogFragment

fun main(activity:FragmentActivity,dialog:RationaleDialogFragment){
    PermissionUtils.init(activity)
        .permissions(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO)
            //设置提示框的高光与暗色
        .setDialogTintColor(Color.parseColor("#1972e8"), Color.parseColor("#8ab6f5"))
            //用户在执行请求之前的操作
        .onExplainRequestReason { scope, deniedList, beforeRequest ->
            val message = "PermissionX needs following permissions to continue"
            scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
//                    val message = "Please allow the following permissions in settings"
//            val dialog = CustomDialogFragment(message, deniedList)
//            scope.showRequestReasonDialog(dialog)
        }
            //用户拒绝之后的操作
        .onForwardToSettings { scope, deniedList ->
//            val message = "Please allow following permissions in settings"
//            val dialog = CustomDialogFragment(message, deniedList)
            scope.showForwardToSettingsDialog(dialog)
        }
            //完成后的回调
        .request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                Toast.makeText(activity, "All permissions are granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "The following permissions are denied：$deniedList", Toast.LENGTH_SHORT).show()
            }
        }
}