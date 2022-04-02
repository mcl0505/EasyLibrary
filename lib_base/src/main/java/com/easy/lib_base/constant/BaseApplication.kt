package com.easy.lib_base.constant

import com.easy.lib_ui.http.HttpRequest
import com.easy.lib_ui.update.UpdateAppUtils
import com.easy.lib_util.app.EasyApplication
import com.easy.lib_util.store.MmkvUtil
import com.easy.lib_util.tool.SignTool

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/12/28
 *   功能描述:
 */
open abstract class BaseApplication : EasyApplication() {
    override fun onCreate() {
        super.onCreate()
        initHttpCommonParameter()
        //App 版本更新
        UpdateAppUtils.init(this)
        //App 基础信息
        SignTool.printSignatureMD5(this)
        //初始化本地信息缓存
        MmkvUtil.init(this)
    }

    /**
     * 设置网络请求的基本信息
     * 可在这个方法中添加  请求头  拦截器
     */
    fun initHttpCommonParameter(){
        //请求地址
        HttpRequest.mDefaultBaseUrl = getHttpHost()
    }
}