package com.hhqc.easylibrary.app

import com.easy.lib_ui.activity.BaseNoViewModelActivity
import com.easy.lib_ui.adapter.BaseDataBindAdapter
import com.easy.lib_ui.dialog.image.SelectImageDialog
import com.easy.lib_ui.http.BaseEntity
import com.easy.lib_ui.mvvm.annotation.VideoModelBR
import com.easy.lib_ui.mvvm.annotation.VideoModelClass
import com.easy.lib_util.dsl.renderString
import com.easy.lib_util.ext.getColor
import com.easy.lib_util.ext.getDrawable
import com.easy.lib_util.ext.singleClick
import com.easy.lib_util.image.loadImage
import com.hhqc.easylibrary.R
import com.hhqc.easylibrary.BR
import com.hhqc.easylibrary.databinding.ActivityMainBinding
import com.hhqc.easylibrary.databinding.ItemMainBinding

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/11/4
 *   功能描述: 主页
 */
@VideoModelClass(vm = MainViewModel::class)
class MainActivity : BaseNoViewModelActivity<ActivityMainBinding>(R.layout.activity_main) {
    var number = 0
    private val mAdapter by lazy { MainAdapter() }

    override fun setTitleText(): String = "首页"

    override fun initData() {
        mBinding.mRecycler.adapter = mAdapter
        mBinding.addData.singleClick {
            val mainEntity = MainEntity("梦虍  $number")
            mainEntity.id = "${number}"
            mAdapter.addList(mutableListOf(mainEntity))
            number += 1
        }
        mBinding.deleteData.singleClick {
            SelectImageDialog{
                mBinding.img.loadImage(it[0])
            }.show(supportFragmentManager)
        }



    }

    inner class MainAdapter : BaseDataBindAdapter<MainEntity, ItemMainBinding>() {
        override fun onBindLayout(viewType: Int): Int = R.layout.item_main

        override fun onBindItem(binding: ItemMainBinding, item: MainEntity, position: Int) {
            binding.name.renderString {
                addText(item.name){
                    setColor(R.color.white.getColor())
                    setSize(20)
                }
                addImg(R.drawable.ic_android.getDrawable())
            }
        }

    }


}

data class MainEntity(
    val name: String = "",
) : BaseEntity()