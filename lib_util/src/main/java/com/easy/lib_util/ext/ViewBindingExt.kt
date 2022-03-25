package com.kckj.baselibrary.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2021/1/20
 * 功能描述：ViewBinding试图绑定扩展函数
 */

fun <T : ViewDataBinding> AppCompatActivity.bindingInflate(inflater: LayoutInflater = layoutInflater, @LayoutRes resId: Int, viewGroup: ViewGroup? = null): T =
    DataBindingUtil.inflate<T>(inflater, resId,viewGroup,false).apply {
        lifecycleOwner = this@bindingInflate
    }



inline fun <T : ViewDataBinding> Fragment.bindingInflate(inflater: LayoutInflater = layoutInflater,@LayoutRes resId: Int,viewGroup: ViewGroup?): T =
    DataBindingUtil.inflate<T>(inflater, resId,viewGroup,false).apply {
        lifecycleOwner = this@bindingInflate
    }