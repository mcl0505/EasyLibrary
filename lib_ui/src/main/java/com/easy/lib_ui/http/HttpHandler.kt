package com.easy.lib_ui.http

import com.easy.lib_util.LogUtil
import com.easy.lib_util.toast.toast
import retrofit2.HttpException
import java.net.SocketTimeoutException


object HttpHandler {

    //请求成功
    const val CODE_HTTP_SUCCESS = 200
    //请求失败
    const val CODE_HTTP_ERROR = 500
    //token 失效
    const val CODE_TOKEN_INVALID = 401
    //NFC 未绑定
    const val CODE_NFC_NO_BIND = 501
    //NFC 当前用户已绑定设备
    const val CODE_NFC_YES_BIND = 502
    //NFC 当设备已绑定其他用户
    const val CODE_NFC_YES_BIND_USER = 503

    /**
     * 处理请求结果
     *
     * [entity] 实体
     * [onSuccess] 状态码对了就回调
     * [onResult] 状态码对了，且实体不是 null 才回调
     * [onFailed] 有错误发生，可能是服务端错误，可能是数据错误，详见 code 错误码和 msg 错误信息
     */
    fun <T> handleResult(
        entity: IBaseResponse<T?>?,
        onSuccess: (() -> Unit)? = null,
        onResult: ((t: T) -> Unit),
        onFailed: ((code: Int, msg: String?) -> Unit)? = {code,msg->
            msg?.toast()
        }
    ) {
        // 防止实体为 null
        if (entity == null) {
            onFailed?.invoke(entityNullable, msgEntityNullable)
            return
        }
        val code = entity.code()
        val msg = entity.msg()
        // 防止状态码为 null
        if (code == null) {
            onFailed?.invoke(entityCodeNullable, msgEntityCodeNullable)
            return
        }
        // 请求成功
        if (entity.isSuccess()) {
            // 回调成功
            onSuccess?.invoke()
            // 实体不为 null 才有价值
            entity.data()?.let { onResult.invoke(it) }
        } else {
            // 失败了
            onFailed?.invoke(code, msg)
        }
    }

    /**
     * 处理异常
     */
    fun handleException(
        e: Exception,
        onFailed: (code: Int, msg: String?) -> Unit
    ) {
        if (LogUtil.isLog()) {
            e.printStackTrace()
        }
        return if (e is HttpException) {
            onFailed(e.code(), e.message())
        } else if (e is SocketTimeoutException){
            LogUtil.d("连接超时")
            onFailed(
                mSocketTimeoutException,
                "$msgSocketTimeoutException"
            )
        }else{
            val log = LogUtil.getStackTraceString(e)
            onFailed(
                notHttpException,
                "$msgNotHttpException, 具体错误是\n${if (log.isEmpty()) e.message else log}"
            )
        }
    }
}