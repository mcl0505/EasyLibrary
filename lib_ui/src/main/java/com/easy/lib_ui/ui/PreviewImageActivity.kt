package com.easy.lib_ui.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.easy.lib_ui.R
import com.easy.lib_ui.activity.BaseNoViewModelActivity
import com.easy.lib_ui.databinding.ActivityPreviewImageBinding
import com.easy.lib_ui.view.PinchImageView
import com.youth.banner.adapter.BannerAdapter


/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/11/4
 *   功能描述:  图片预览
 *
 *   传入   图片列表   位置
 *
 */
class PreviewImageActivity :
    BaseNoViewModelActivity<ActivityPreviewImageBinding>(R.layout.activity_preview_image) {
    private val mAdapter by lazy { PreviewImageAdapter(ArrayList()) }
    override fun initData() {
        super.initData()
        mAdapter.setNewList(intent.getStringArrayListExtra("imageArray") as MutableList<String>)
        mBinding.mBanner.apply {
            addBannerLifecycleObserver(this@PreviewImageActivity)
            setStartPosition(intent.getIntExtra("position",0)+1)
            adapter = mAdapter
        }

    }

    //轮播图列表
    inner class PreviewImageAdapter(list: MutableList<String>) :
        BannerAdapter<String, PreviewImageHolder>(list) {
        override fun onCreateHolder(parent: ViewGroup?, viewType: Int): PreviewImageHolder {
            val imageView = PinchImageView(parent!!.context)
            //注意，必须设置为match_parent，这个是viewpager2强制要求的
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
            imageView.layoutParams = params
            return PreviewImageHolder(imageView)
        }

        override fun onBindView(
            holder: PreviewImageHolder?,
            data: String?,
            position: Int,
            size: Int,
        ) {
            (holder?.itemView as PinchImageView).load(data)
            (holder?.itemView as PinchImageView).setOnClickListener {
                finish()
            }
        }

        public fun setNewList(list: MutableList<String>) {
            mDatas.clear()
            mDatas.addAll(list)
            notifyDataSetChanged()
        }

    }

    inner class PreviewImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}