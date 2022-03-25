package com.easy.lib_ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 创建时间：2021/4/22
 * 功能描述：不包含标题的适配器
 */
class BaseViewPagerAdapter(manager: FragmentManager, val fragment:List<Fragment>) : FragmentPagerAdapter(manager){
    override fun getCount(): Int = fragment.size

    override fun getItem(position: Int): Fragment = fragment.get(position)
}