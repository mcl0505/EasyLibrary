package com.easy.lib_util.permission.request

import com.easy.lib_util.permission.dialog.RationaleDialog
import com.easy.lib_util.permission.dialog.RationaleDialogFragment
import kotlin.jvm.JvmOverloads

/**
 * Provide specific scopes for [com.permissionx.guolindev.callback.ExplainReasonCallback]
 * and [com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam] to give it specific functions to call.
 * @author guolin
 * @since 2020/3/18
 */
class ExplainScope internal constructor(
    private val pb: PermissionBuilder,
    private val chainTask: ChainTask
) {
    /**
     * Show a rationale dialog to explain to user why you need these permissions.
     * @param permissions
     * Permissions that to request.
     * @param message
     * Message that show to user.
     * @param positiveText
     * Text on the positive button. When user click, PermissionX will request permissions again.
     * @param negativeText
     * Text on the negative button. When user click, PermissionX will finish request.
     */
    @JvmOverloads
    fun showRequestReasonDialog(permissions: List<String>, message: String, positiveText: String, negativeText: String? = null) {
        pb.showHandlePermissionDialog(chainTask, true, permissions, message, positiveText, negativeText)
    }

    /**
     * Show a rationale dialog to explain to user why you need these permissions.
     * @param dialog
     * Dialog to explain to user why these permissions are necessary.
     */
    fun showRequestReasonDialog(dialog: RationaleDialog) {
        pb.showHandlePermissionDialog(chainTask, true, dialog)
    }

    /**
     * Show a rationale dialog to explain to user why you need these permissions.
     * @param dialogFragment
     * DialogFragment to explain to user why these permissions are necessary.
     */
    fun showRequestReasonDialog(dialogFragment: RationaleDialogFragment) {
        pb.showHandlePermissionDialog(chainTask, true, dialogFragment)
    }
}