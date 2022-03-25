package com.easy.lib_ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easy.lib_util.ext.singleClick


import java.util.*
import kotlin.collections.ArrayList


abstract class BaseAdapter<E, VH : BaseViewHolder>() :
        RecyclerView.Adapter<VH>() {
    protected var mList: MutableList<E>
    protected var mContext: Context? = null

    //mNewsTopAdapter.onViewClick = {item, position ->  }
    lateinit var onViewClick: (item:E,position:Int) ->Unit// 无参 括号中不写

    val listData: MutableList<E>
        get() = mList

    init {
        mList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        if (mContext ==null){
            mContext = parent.context
        }

        val view = LayoutInflater.from(mContext).inflate(onBindLayout(viewType), parent, false)
        val onCreateHolder = onCreateHolder(view)
        onCreateHolder.adapter = this
        return onCreateHolder
    }

    /**
     * 绑定布局文件
     */
    protected abstract fun onBindLayout(viewType: Int): Int

    /**
     * 创建一个holder
     */
    protected fun onCreateHolder(view: View): VH = BaseViewHolder(itemView = view) as VH

    /**
     * 绑定数据
     */
    protected abstract fun onBindItem(holder: VH, item: E, positon: Int)

    override fun onBindViewHolder(holder: VH, position: Int) {
        val e = mList[position]
        holder.itemView.singleClick {
            if (::onViewClick.isInitialized)onViewClick.invoke(e,position)
        }
        onBindItem(holder, e, position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    open fun setNewList(list: MutableList<E>) {
        refresh(list)
    }

    fun addAll(list: MutableList<E>) {
        if (list.isNotEmpty()) {
            mList.clear()
            mList.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun addList(list: MutableList<E>) {
        if (list.isNotEmpty()) {
            val li: MutableList<E> = ArrayList()
            li.addAll(list)

            for (t in li) {
                mList.add(t)
            }

            notifyDataSetChanged()
        }
    }

    fun refresh(list: List<E>) {
        mList.clear()
        if (list.isNotEmpty()) {
            mList.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun remove(positon: Int) {
        mList.removeAt(positon)
        notifyDataSetChanged()
    }

    fun remove(e: E) {
        mList.remove(e)
        notifyDataSetChanged()
    }

    fun add(e: E) {
        mList.add(e)
        notifyDataSetChanged()
    }

    fun addLast(e: E) {
        add(e)
    }

    fun addFirst(e: E) {
        mList.add(0, e)
        notifyDataSetChanged()
    }

    fun clear() {
        mList.clear()
        notifyDataSetChanged()
    }

}
