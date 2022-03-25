package com.easy.lib_util.ext

import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.easy.lib_util.app.EasyApplication

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2020/11/4
 * 功能描述：获取资源信息
 */

//获取颜色
fun Int.getColor() : Int = EasyApplication.getContext().resources.getColor(this)

fun Int.getDrawable() : Drawable = EasyApplication.getContext().resources.getDrawable(this)

fun Int.getString() : String = EasyApplication.getContext().resources.getString(this)

fun Int.getStringArray() : List<String> = EasyApplication.getContext().resources.getStringArray(this).toList() as MutableList<String>

// Int  代表想表达的大小
//PX  1px= 1像素 屏幕的分辨率= 宽向像素数X纵向像素数
//DP  px= dp （dpi/160）
//SP  sp 主要用作字体的单位
fun Int.toDp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Double.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Double.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()