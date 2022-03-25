package com.easy.lib_ui.update

import android.app.Activity
import android.app.AlertDialog
import com.easy.lib_ui.R
import com.easy.lib_util.ext.getString

/**
 * desc: AlertDialogUtil
 * time: 2018/8/20
 * @author teprinciple
 */
internal object AlertDialogUtil {

    fun show(
        activity: Activity,
        message: String,
        onCancelClick: () -> Unit = {},
        onSureClick: () -> Unit = {},
        cancelable: Boolean = false,
        title: String = R.string.notice.getString(),
        cancelText: String = R.string.cancel.getString(),
        sureText: String = R.string.sure.getString()
    ) {
        AlertDialog.Builder(activity, R.style.AlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(sureText) { _, _ ->
                onSureClick.invoke()
            }
            .setNegativeButton(cancelText) { _, _ ->
                onCancelClick.invoke()
            }
            .setCancelable(cancelable)
            .create()
            .show()
    }
}