package com.easy.lib_util.app

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.kingja.loadsir.core.LoadSir
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
        //注册整个app的生命周期
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
