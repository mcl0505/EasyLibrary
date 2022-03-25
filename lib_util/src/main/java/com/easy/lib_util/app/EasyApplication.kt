package com.easy.lib_util.app

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.easy.lib_util.AppImp
import com.easy.lib_util.executor.AppExecutorsHelper
import com.easy.lib_util.store.MmkvUtil
import com.easy.lib_util.tool.SignTool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * 程序入口  基类
 */
abstract class EasyApplication : MultiDexApplication() , AppImp {
    //是否开启
    open val isDebug = true

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppManager.register(this)
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            initOtherSDK()
            //收集异常信息  保存到手机中方便查看
            CrashHandlerUtil.init()
        }
    }

    companion object {
        lateinit var instance: EasyApplication
        fun getContext() = instance.applicationContext

    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //初始化方法查出最大数处理
        MultiDex.install(this)
    }



}
