package com.easy.lib_util.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2021/3/29
 * 功能描述：数据观察
 */
fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit,actionNull: () -> Unit = {}) {
    liveData.observe(this, Observer {
        if (it == null){
            actionNull()
        }else {
            action(it)
        }
    })
}