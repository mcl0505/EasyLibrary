package com.easy.lib_util.permission.request;

import java.util.List;

/**
 * Define a task interface to request permissions.
 * Not all permissions can be requested at one time. Some permissions need to request separately.
 * So each permission request need to implement this interface, and do the request logic in their implementations.
 *
 * @author guolin
 * @since 2020/6/10
 */
public interface ChainTask {

    /**
     * Get the ExplainScope for showing RequestReasonDialog.
     * @return Instance of ExplainScope.
     */
    ExplainScope getExplainScope();

    /**
     * Get the ForwardScope for showing ForwardToSettingsDialog.
     * @return Instance of ForwardScope.
     */
    ForwardScope getForwardScope();

    /**
     * Do the request logic.
     */
    void request();

    /**
     * Request permissions again when user denied.
     * @param permissions
     *          Permissions to request again.
     */
    void requestAgain(List<String> permissions);

    /**
     * Finish this task and notify the request result.
     */
    void finish();
}