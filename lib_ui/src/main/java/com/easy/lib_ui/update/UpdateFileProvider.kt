package com.easy.lib_ui.update

import androidx.core.content.FileProvider
import com.easy.lib_util.ext.yes

/**
 * desc: UpdateFileProvider
 * time: 2019/7/10
 * @author Teprinciple
 */
class UpdateFileProvider : FileProvider() {
    override fun onCreate(): Boolean {
        val result = super.onCreate()
        (GlobalContextProvider.mContext == null && context != null).yes {
            GlobalContextProvider.mContext = context
//            LogUtil.d("内部Provider初始化context：" + GlobalContextProvider.mContext)
        }
        return result
    }
}