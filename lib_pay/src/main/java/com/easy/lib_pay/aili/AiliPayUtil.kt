package com.easy.lib_pay.aili

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.alipay.sdk.app.AuthTask
import com.alipay.sdk.app.PayTask


/**-
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：AiliPayUtil
 * 创建时间：2020/10/7
 * 功能描述：支付宝相关操作
 */
object AiliPayUtil {
    //支付标识
    const val SDK_PAY_FLAG = 1
    //登录标识
    const val SDK_AUTH_FLAG = 2
    private lateinit var mActivity: Activity

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)

                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo: String = payResult.result!! // 同步返回需要验证的信息
                    val resultStatus: String = payResult.resultStatus!!
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        LiveDataBus.send("paySuccess","1")
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        showAlert(mActivity, "支付成功")
//                        "取消支付".toast()
                    }
                }
                SDK_AUTH_FLAG -> {
                    val authResult = AuthResult(msg.obj as Map<String?, String?>, true)
                    val resultStatus: String = authResult.resultStatus!!

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.resultStatus!!, "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的valuez
                        // 传入，则支付账户为该授权账户
                        showAlert(mActivity, "授权成功")
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(mActivity, "授权失败")
                    }
                }
                else -> {
                }
            }
        }
    }

    /**
     * 支付宝支付
     * @param mActivity
     * @param info 支付信息
     * @param handler
     */
    fun aliPay(mActivity: Activity, info: String, handler: Handler = mHandler) {
        AiliPayUtil.mActivity = mActivity
        val payRunnable = Runnable {
            val alipay = PayTask(mActivity)
            val result: Map<String, String> =
                alipay.payV2(info, true)
            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            handler.sendMessage(msg)
        }
        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()
    }

    /**
     * 支付宝登录授权
     */
    fun aliLogin(mActivity: Activity,authInfo: String, handler: Handler = mHandler){
        AiliPayUtil.mActivity = mActivity
        val authRunnable = Runnable { // 构造AuthTask 对象
            val authTask = AuthTask(mActivity)
            // 调用授权接口，获取授权结果
            val result = authTask.authV2(authInfo, true)
            val msg = Message()
            msg.what = SDK_AUTH_FLAG
            msg.obj = result
            handler.sendMessage(msg)
        }

        // 必须异步调用
        val authThread = Thread(authRunnable)
        authThread.start()
    }

    private fun showAlert(ctx: Context, info: String) {
        showAlert(ctx, info, null)
    }

    private fun showAlert(
        ctx: Context,
        info: String,
        onDismiss: DialogInterface.OnDismissListener?
    ) {
        AlertDialog.Builder(ctx)
            .setMessage(info)
            .setPositiveButton("提交的信息", null)
            .setOnDismissListener(onDismiss)
            .show()
    }

    private fun showToast(ctx: Context, msg: String) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
    }

    private fun bundleToString(bundle: Bundle?): String? {
        if (bundle == null) {
            return "null"
        }
        val sb = StringBuilder()
        for (key in bundle.keySet()) {
            sb.append(key).append("=>").append(bundle[key]).append("\n")
        }
        return sb.toString()
    }
}