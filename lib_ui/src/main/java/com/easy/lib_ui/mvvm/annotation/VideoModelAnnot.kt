package com.easy.lib_ui.mvvm.annotation

import com.easy.lib_ui.mvvm.model.BaseModel
import com.easy.lib_ui.mvvm.viewmodel.BaseViewModel
import kotlin.reflect.KClass


/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/03/29
 *   功能描述: ViewModel 在Activity  Fragment 中使用的注解
 */
@Target(AnnotationTarget.CLASS)//可以给标签自己贴标签
annotation class VideoModelClass(val vm : KClass<*>  )

@Target(AnnotationTarget.CLASS)//可以给标签自己贴标签
annotation class VideoModelBR( val br : Int )