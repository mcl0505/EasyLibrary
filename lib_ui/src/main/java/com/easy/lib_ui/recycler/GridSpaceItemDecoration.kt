package com.easy.lib_ui.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

/**
 * 公司名称: ~漫漫人生路~总得错几步~
 * 创建作者: Android 孟从伦
 * 创建时间: 2021/10/27
 * 功能描述: 表格布局管理器分割线
 */
class GridSpaceItemDecoration
/**
 * @param spanCount     列数
 * @param rowSpacing    行间距
 * @param columnSpacing 列间距
 */(
//横条目数量
    private val mSpanCount: Int, //行间距
    private val mRowSpacing: Int, // 列间距
    private val mColumnSpacing: Int
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //获取当前位置
        val position = parent.getChildAdapterPosition(view) // 获取view 在adapter中的位置。
        val column = position % mSpanCount // view 所在的列
        val manager = parent.layoutManager as GridLayoutManager?
        val spanCount = manager!!.spanCount //获取每行的个数
        outRect.top = mRowSpacing
        if (position % 2 == 1) { //当每行个数为2个时    1为左侧   2为右侧
            outRect.right = mColumnSpacing
        } else {
            outRect.left = mColumnSpacing
        }
    }
}