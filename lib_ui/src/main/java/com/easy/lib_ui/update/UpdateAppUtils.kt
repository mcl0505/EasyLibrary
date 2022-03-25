package com.easy.lib_ui.update

import android.content.Context
import com.easy.lib_ui.update.model.UiConfig
import com.easy.lib_ui.update.model.UpdateConfig
import com.easy.lib_ui.update.model.UpdateInfo
import com.easy.lib_util.LogUtil
import com.easy.lib_util.store.MmkvUtil
import com.easy.lib_util.ext.no
import com.easy.lib_util.ext.yes


/**
 * 更新App 工具类   https://github.com/teprinciple/UpdateAppUtils
 * 可以更具  updateConfig   uiConfig   配置不同的功能与样式
 * 简易模式：
        UpdateAppUtils
            .getInstance()
            .apkUrl(apkUrl)//apk地址
            .updateContent("发现新版本") //跟新内容
            .update()
 *
 */
object UpdateAppUtils {


    // 更新信息对象
    internal val updateInfo by lazy { UpdateInfo() }

    // 下载监听
    internal var downloadListener: UpdateDownloadListener? = null

    // md5校验结果回调
    internal var md5CheckResultListener: Md5CheckResultListener? = null

    // 初始化更新弹窗UI回调
    internal var onInitUiListener: OnInitUiListener? = null

    // "暂不更新"按钮点击事件
    internal var onCancelBtnClickListener: OnBtnClickListener? = null

    // "立即更新"按钮点击事件
    internal var onUpdateBtnClickListener: OnBtnClickListener? = null

    /**
     * 设置apk下载地址
     */
    fun apkUrl(apkUrl: String): UpdateAppUtils {
        updateInfo.apkUrl = apkUrl
        return this
    }

    var isShow = false

    /**
     * 服务器versionCode
     */
    fun serverVersionCode(code:Int):UpdateAppUtils{
        updateInfo.config.serverVersionCode = code
        return this
    }

    /**
     * 服务器versionName
     */
    fun serverVersionName(name:String):UpdateAppUtils{
        updateInfo.config.serverVersionName = name
        return this
    }

    fun layoutRes(res: Int): UpdateAppUtils {
        updateInfo.uiConfig.customLayoutId = res
        return this
    }

    /**
     * 设置更新标题
     */
    fun updateTitle(title: CharSequence): UpdateAppUtils {
        updateInfo.updateTitle = title
        return this
    }

    /**
     * 设置更新内容
     */
    fun updateContent(content: CharSequence): UpdateAppUtils {
        updateInfo.updateContent = content
        return this
    }

    /**
     * 设置更新配置
     */
    fun updateConfig(config: UpdateConfig): UpdateAppUtils {
        updateInfo.config = config
        return this
    }

    /**
     * 设置UI配置
     */
    fun uiConfig(uiConfig: UiConfig): UpdateAppUtils {
        updateInfo.uiConfig = uiConfig
        return this
    }

    /**
     * 设置下载监听
     */
    fun setUpdateDownloadListener(listener: UpdateDownloadListener?): UpdateAppUtils {
        this.downloadListener = listener
        return this
    }

    /**
     * 设置md5校验结果监听
     */
    fun setMd5CheckResultListener(listener: Md5CheckResultListener?): UpdateAppUtils {
        this.md5CheckResultListener = listener
        return this
    }

    /**
     * 设置初始化UI监听
     */
    fun setOnInitUiListener(listener: OnInitUiListener?): UpdateAppUtils {
        this.onInitUiListener = listener
        return this
    }

    /**
     * 设置 “暂不更新” 按钮点击事件
     */
    fun setCancelBtnClickListener(listener: OnBtnClickListener?): UpdateAppUtils {
        this.onCancelBtnClickListener = listener
        return this
    }

    /**
     * 设置 “立即更新” 按钮点击事件
     */
    fun setUpdateBtnClickListener(listener: OnBtnClickListener?): UpdateAppUtils {
        this.onUpdateBtnClickListener = listener
        return this
    }

    /**
     * 检查更新
     */
    fun update() {

        if (globalContext() == null) {
            LogUtil.d("请先调用初始化init")
            return
        }

        val keyName = (globalContext()?.packageName ?: "") + updateInfo.config.serverVersionName
        // 设置每次显示，设置本次显示及强制更新 每次都显示弹窗
        (updateInfo.config.alwaysShow || updateInfo.config.thisTimeShow || updateInfo.config.force).yes {
            UpdateAppActivity.launch()
        }.no {
            val hasShow = MmkvUtil.getValue(keyName, false)
            (hasShow).no { UpdateAppActivity.launch() }
        }
        MmkvUtil.putValue(keyName, true)
    }

    /* 未缓存apk
    /**
     * 删除已安装 apk
     */
    fun deleteInstalledApk() {
        val apkPath = SPUtil.getString(DownloadAppUtils.KEY_OF_SP_APK_PATH, "")
        val appVersionCode = Utils.getAPPVersionCode()
        val apkVersionCode = Utils.getApkVersionCode(apkPath)
        log("appVersionCode:$appVersionCode")
        log("apkVersionCode:$apkVersionCode")
        (apkPath.isNotEmpty() && appVersionCode == apkVersionCode && apkVersionCode > 0).yes {
            Utils.deleteFile(apkPath)
        }
    }
     */

    /**
     * 获取单例对象
     */
    @JvmStatic
    fun getInstance() = this

    /**
     * 初始化，非必须。解决部分手机 通过UpdateFileProvider 获取不到context情况使用
     * * @param context 提供全局context。
     */
    @JvmStatic
    fun init(context: Context) {
        GlobalContextProvider.mContext = context.applicationContext
//        LogUtil.d("外部初始化context")
    }
}