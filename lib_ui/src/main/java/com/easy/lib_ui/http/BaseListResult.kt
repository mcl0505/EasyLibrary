package com.easy.lib_ui.http

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/12/14
 *   功能描述: 分页返回数据格式   外层包含BaseResult
 */
data class BaseListResult<T>(
    //数据列表
    val rows:MutableList<T> = ArrayList()
)
