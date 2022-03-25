package com.easy.lib_util.ext

import com.easy.lib_util.LogUtil
import java.io.File

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/12/22
 *   功能描述:
 */
/**
 * 根据文件路径删除文件
 */
fun String?.deleteFile() {
    kotlin.runCatching {
        val file = File(this ?: "")
        (file.isFile).yes {
            file.delete()
            LogUtil.d("删除成功")
        }
    }.onFailure {
        LogUtil.d(it.message!!)
    }
}