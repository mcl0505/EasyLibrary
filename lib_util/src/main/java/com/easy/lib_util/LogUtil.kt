package com.easy.lib_util

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import com.easy.lib_util.app.EasyApplication.Companion.instance
import com.easy.lib_util.app.AppUtil
import com.easy.lib_util.app.CrashHandlerUtil
import com.easy.lib_util.app.EasyApplication
import com.easy.lib_util.app.FileUtil
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*


/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/12/8
 *   功能描述: 日志打印工具
 */
object LogUtil {

    /**
     * 是否保存 log 到缓存目录。目录地址：
     * /sdcard/Android/data/应用包名/cache/Log
     *
     * 建议打包提测的都开启保存。比如 beta 构建选项
     */
    var gIsSaveLog = false

    private const val V = 0x1
    private const val D = 0x2
    private const val I = 0x3
    private const val W = 0x4
    private const val E = 0x5
    private const val A = 0x6

    private const val TOP_BORDER =
        "╔══════打印开始═════════════════════════════════════════════════════════════════════════════════════════════"
    private const val LEFT_BORDER = "║ "
    private const val BOTTOM_BORDER =
        "╚══════打印结束═════════════════════════════════════════════════════════════════════════════════════════════"


    @SuppressLint("HandlerLeak")
    private lateinit var mHandler: Handler
    private lateinit var mBufferWrite: BufferedWriter
    private lateinit var mCurDateTime: String

    @JvmStatic
    internal fun init() {
        CrashHandlerUtil.init()
    }

    @Synchronized
    private fun initLogHandler() {
        if (this::mHandler.isInitialized) {
            return
        }
        val handlerThread = HandlerThread("log")
        handlerThread.start()

        mCurDateTime = DateUtil.formatYMD_()
        mBufferWrite = BufferedWriter(
            OutputStreamWriter(
                FileOutputStream(
                    FileUtil.appLogDir + mCurDateTime + ".log",
                    true
                )
            )
        )
        mBufferWrite.write(getDeviceInfo())

        mHandler = object : Handler(handlerThread.looper) {
            override fun handleMessage(msg: Message) {
                val dateTime = DateUtil.formatYMD_()
                if (dateTime != mCurDateTime) {
                    if (this@LogUtil::mBufferWrite.isInitialized) {
                        mBufferWrite.close()
                    }
                    mCurDateTime = dateTime
                    mBufferWrite = BufferedWriter(
                        OutputStreamWriter(
                            FileOutputStream(
                                FileUtil.appLogDir + mCurDateTime + ".log",
                                true
                            )
                        )
                    )
                    mBufferWrite.write(getDeviceInfo())
                }

                mBufferWrite.write(DateUtil.formatYMDHMS_SSS() + "/" + msg.obj.toString())
                mBufferWrite.newLine()
                mBufferWrite.flush()
            }
        }
    }

    private fun getDeviceInfo(): String {
        val lineSeparator = "\r\n"
        val sb = StringBuilder()
        sb.append(lineSeparator)
        sb.append(lineSeparator)

        sb.append("appVerName:").append(AppUtil.getAppVersionName(instance)).append(lineSeparator)
        sb.append("appVerCode:").append(AppUtil.getAppVersionCode(instance)).append(lineSeparator)
        sb.append("buildType:").append(BuildConfig.BUILD_TYPE).append(lineSeparator)
        // 系统版本
        sb.append("OsVer:").append(Build.VERSION.RELEASE).append(lineSeparator)
        // 手机厂商
        sb.append("vendor:").append(Build.MANUFACTURER).append(lineSeparator)
        // 型号
        sb.append("model:").append(Build.MODEL).append(lineSeparator)
        sb.append(lineSeparator)
        sb.append(lineSeparator)
        return sb.toString()
    }

    @JvmStatic
    fun v(msg: Any?) {
        printLog(V, null, msg)
    }

    @JvmStatic
    fun v(tag: String?, msg: String?) {
        printLog(V, tag, msg)
    }

    @JvmStatic
    fun d(msg: Any?) {
        printLog(D, null, msg)
    }

    @JvmStatic
    fun d(tag: String?, msg: Any?) {
        printLog(D, tag, msg)
    }

    @JvmStatic
    fun i(msg: Any?) {
        printLog(I, null, msg)
    }

    @JvmStatic
    fun i(tag: String?, msg: Any?) {
        printLog(I, tag, msg)
    }

    @JvmStatic
    fun w(msg: Any?) {
        printLog(W, null, msg)
    }

    @JvmStatic
    fun w(tag: String?, msg: Any?) {
        printLog(W, tag, msg)
    }

    @JvmStatic
    fun e(msg: Any?) {
        printLog(E, null, msg)
    }

    @JvmStatic
    fun e(tag: String?, msg: Any?) {
        printLog(E, tag, msg)
    }

    @JvmStatic
    fun a(msg: Any?) {
        printLog(A, null, msg)
    }

    @JvmStatic
    fun a(tag: String?, msg: Any?) {
        printLog(A, tag, msg)
    }

    fun isLog() = EasyApplication.instance.isDebug

    private fun printLog(type: Int, tagStr: String?, objectMsg: Any?) {
        if (!isLog()) {
            return
        }
        val stackTrace =
            Thread.currentThread().stackTrace
        val index = 4
        val className = stackTrace[index].fileName
        var methodName = stackTrace[index].methodName
        val lineNumber = stackTrace[index].lineNumber
        val tag = tagStr ?: className
        methodName = methodName.substring(0, 1).toUpperCase(Locale.getDefault()) + methodName.substring(1)
        val stringBuilder = StringBuilder()
        stringBuilder.append("LogUtil ==>[ (").append(className).append(":").append(lineNumber).append(")#")
            .append(methodName).append(" ] \n${TOP_BORDER}\n")
        val msg = objectMsg?.toString() ?: "Log with null Object"
        stringBuilder.append(msg)
        stringBuilder.append("\n${BOTTOM_BORDER}\n")
        val logStr = stringBuilder.toString()
        when (type) {
            V -> Log.v(tag, logStr)
            D -> Log.d(tag, logStr)
            I -> Log.i(tag, logStr)
            W -> Log.w(tag, logStr)
            E -> Log.e(tag, logStr)
            A -> Log.wtf(tag, logStr)
        }
        // 是否保存到本地缓存目录
        if (!this::mHandler.isInitialized && gIsSaveLog) {
            initLogHandler()
        }
        // 已初始化，则可以发送消息了
        if (this::mHandler.isInitialized) {
            val m = Message.obtain()
            m.obj = "$tag = $logStr"
            mHandler.sendMessage(m)
        }
    }

    @JvmStatic
    fun getStackTraceString(t: Throwable): String {
        return Log.getStackTraceString(t)
    }


}

