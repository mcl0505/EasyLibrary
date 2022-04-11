package com.easy.lib_ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2022/04/11
 *   功能描述: ViewPager2 适配器
 */
class ViewPagerAdapter(fa: FragmentActivity, val frame:MutableList<Fragment>) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return frame.size
    }

    override fun createFragment(position: Int): Fragment {
        return frame[position]
    }

}