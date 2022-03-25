package com.easy.lib_ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.easy.lib_ui.adapter.AddImgAdapter.FooterViewHolder
import com.easy.lib_ui.adapter.AddImgAdapter.ContentViewHolder
import com.easy.lib_ui.adapter.AddImgAdapter.OnAddImgListener
import android.view.ViewGroup
import com.easy.lib_ui.adapter.AddImgAdapter
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import coil.load
import com.easy.lib_ui.R
import com.makeramen.roundedimageview.RoundedImageView
import java.util.ArrayList

/**
 * 公司名称: ~漫漫人生路~总得错几步~
 * 创建作者: Android 孟从伦
 * 创建时间: 2021/11/22
 * 功能描述:  图片添加适配器
 */
class AddImgAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    private val mList: MutableList<String> = ArrayList()
    private var mContext: Context? = null
    private var footerViewHolder: FooterViewHolder? = null
    private var contentViewHolder: ContentViewHolder? = null
    private var maxPosition = 5
    var imgAdd: ImageView? = null
    var img: ImageView? = null
    var imgDelete: ImageView? = null
    private var onAddImgListener: OnAddImgListener? = null
    fun setOnAddImgListener(onAddImgListener: OnAddImgListener?) {
        this.onAddImgListener = onAddImgListener
    }

    fun remove() {
        mList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (mContext == null) {
            mContext = parent.context
        }
        var vh: BaseViewHolder? = null
        val view: View
        if (viewType == TYPE_FOOTER_VIEW) {
            view =
                LayoutInflater.from(mContext).inflate(R.layout.item_add_img_footer, parent, false)
            vh = FooterViewHolder(view)
        } else {
            view =
                LayoutInflater.from(mContext).inflate(R.layout.item_add_img_content, parent, false)
            vh = ContentViewHolder(view)
        }
        return vh
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder != null) {
            if (holder is ContentViewHolder) {
                contentViewHolder = holder
                img = contentViewHolder!!.img
                imgDelete = contentViewHolder!!.imgDelete
                (img as RoundedImageView).load(mList[position])
                //点击删除监听
                imgDelete!!.setOnClickListener { v: View? ->
                    if (onAddImgListener != null) {
                        onAddImgListener!!.onClickDelete(position)
                    }
                }
            } else {
                footerViewHolder = holder as FooterViewHolder
                imgAdd = footerViewHolder!!.imgAdd
                //点击添加监听
                imgAdd!!.setOnClickListener { v: View? ->
                    if (onAddImgListener != null) {
                        onAddImgListener!!.onClickAdd()
                    }
                }
            }
        }

        if (mList.size == maxPosition) {
            footerViewHolder!!.imgAdd.visibility = View.GONE
        } else {
            footerViewHolder!!.imgAdd.visibility = View.VISIBLE
        }
    }

    fun setMaxPosition(maxPosition: Int) {
        this.maxPosition = maxPosition
    }

    override fun getItemCount(): Int {
        return mList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        /*当position是最后一个的时候，也就是比list的数量多一个的时候，则表示FooterView*/
        return if (position + 1 == mList.size + 1) {
            TYPE_FOOTER_VIEW
        } else super.getItemViewType(position)
    }

    internal inner class FooterViewHolder(itemView: View) : BaseViewHolder(itemView) {
        var imgAdd: ImageView

        init {
            imgAdd = itemView.findViewById(R.id.item_push_share_add)
        }
    }

    internal inner class ContentViewHolder(itemView: View) : BaseViewHolder(itemView) {
        var img: RoundedImageView
        var imgDelete: ImageView

        init {
            img = itemView.findViewById(R.id.item_push_share_imageview)
            imgDelete = itemView.findViewById(R.id.item_push_share_img_cancel)
        }
    }

    //  添加数据
    fun addData(str: String) {
//      在list中添加数据，并通知条目加入一条
        mList.add(str)
        //添加动画
        notifyDataSetChanged()
    }

    //  添加数据
    fun addDataList(str: MutableList<String>) {
//      在list中添加数据，并通知条目加入一条
        str.forEach {
            mList.add(it)
        }

        //添加动画
        notifyDataSetChanged()
    }


    var list: MutableList<String>?
        get() = mList
        set(list) {
            mList.addAll(list!!)
            notifyDataSetChanged()
        }

    fun removeItem(posi: Int) {
        mList.removeAt(posi)
        notifyItemRemoved(posi)
        notifyDataSetChanged()
    }

    interface OnAddImgListener {
        /**
         * 点击添加图片
         */
        fun onClickAdd()

        /**
         * 点击删除图片
         * @param position
         */
        fun onClickDelete(position: Int)
    }

    companion object {
        //底部视图标识
        const val TYPE_FOOTER_VIEW = 1
    }

}