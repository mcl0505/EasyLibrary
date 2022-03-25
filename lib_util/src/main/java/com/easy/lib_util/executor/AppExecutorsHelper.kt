package com.easy.lib_util.executor

import com.easy.library.utils.AppExecutors
import java.util.concurrent.TimeUnit

/**
 * 公司：     
 * 作者：Android 孟从伦
 * 创建时间：2021/5/6
 * 功能描述：
 */
object AppExecutorsHelper {

    /**
     * 更新UI 线程
     */
    fun uiHandler(block: () -> Unit) {
        AppExecutors.mainThread().execute{
            block()
        }
    }

    /**
     * 更新UI 线程
     */
    fun ioHandler(block: () -> Unit) {
        AppExecutors.diskIO().execute{
            block()
        }
    }

    /**
     * 更新UI 线程
     */
    fun httpHandler(block: () -> Unit) {
        AppExecutors.networkIO().execute{
            block()
        }
    }

    /**
     * 延时
     * @param block 执行的方法
     * @param delayMillis 延时的时间  秒
     */
    fun postDelayed(block: () -> Unit, delayMillis: Long = 2) {
        AppExecutors.schedule().schedule({ block() }, delayMillis, TimeUnit.SECONDS)
    }

    /**
     * 每个特定时间执行任务
     * @param block 执行的方法
     * @param delayMillis 延时的时间  秒
     */
    fun postDelayedTime(block: () -> Unit, delayMillis: Long = 2){
        //command：执行线程    initialDelay：初始化延时 period：两次开始执行最小间隔时间   unit：计时单位
        AppExecutors.schedule().scheduleAtFixedRate({block()},0,delayMillis*1000,TimeUnit.MILLISECONDS)
    }


}