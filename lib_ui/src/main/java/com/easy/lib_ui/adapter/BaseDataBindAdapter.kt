package com.easy.lib_ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseDataBindAdapter<E, V : ViewDataBinding>() :
    BaseAdapter<E, BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (mContext == null){
            mContext = parent.context
        }

        val binding: V = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            onBindLayout(viewType),
            parent,
            false
        )
        val onCreateHolder = onCreateHolder(binding.root)
        onCreateHolder.adapter = this
        return onCreateHolder
    }

    override fun onBindItem(holder: BaseViewHolder, e: E, positon: Int) {
        val binding = DataBindingUtil.getBinding<V>(holder.itemView)
        onBindItem(binding!!, e, positon)
    }

    protected abstract fun onBindItem(binding: V, item: E, position: Int)


}
