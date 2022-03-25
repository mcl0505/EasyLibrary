package com.easy.lib_util.permission.callback;

import androidx.annotation.NonNull;

import com.easy.lib_util.permission.request.ExplainScope;

import java.util.List;

/**
 * Callback for {@link PermissionBuilder#onExplainRequestReason(ExplainReasonCallback)} method.
 *
 * @author guolin
 * @since 2020/6/7
 */
public interface ExplainReasonCallback {

    /**
     * Called when you should explain why you need these permissions.
     * @param scope
     *          Scope to show rationale dialog.
     * @param deniedList
     *          Permissions that you should explain.
     */
    void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList);

}
