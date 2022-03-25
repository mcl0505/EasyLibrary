package com.easy.lib_ui.tab.tablayout

import android.content.res.Resources
import android.widget.TextView
import com.easy.lib_ui.R
import com.google.android.material.tabs.TabLayout




/**
 * 绑定tabLayout指示器
 */
inline fun <reified T : BaseIndicator> TabLayout.buildIndicator():T {
    val indicator = T::class.java.newInstance()
    indicator.bindTabLayout(this)
    return indicator
}

/**
 * 绑定tabLayout指示器
 */
inline fun  TabLayout.setTitle(mList:List<String>) {
   mList.forEach {
       val tab = newTab()
       tab.setCustomView(R.layout.tablayout_item)
       if (tab.customView != null) {
           val tab_text = tab.customView!!.findViewById<TextView>(R.id.tab_text)
           tab_text.text = it
       }
       this.addTab(tab)
   }
}

/**
 * 绑定tabLayout文字设置
 */
inline fun <reified T : BaseText> TabLayout.buildText():T {
    val text = T::class.java.newInstance()
    text.bindTabLayout(this)
    return text
}