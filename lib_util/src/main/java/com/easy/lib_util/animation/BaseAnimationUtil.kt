package com.easy.lib_util.animation

import android.content.Context
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.easy.lib_util.R

/**
 * 公司：     
 * 作者：Android 孟从伦
 * 创建时间：2021/3/24
 * 功能描述：布局动画
 */
object BaseAnimationUtil {
    /**
     * 从顶部下落的动画
     * @param mViewGroup 需要添加动画的布局
     */
    fun AnimationTopBottom(mViewGroup: ViewGroup){
        val mLayoutAnimationController = AnimationUtils.loadLayoutAnimation(mViewGroup.context, R.anim.layout_animation_from_top)
        mViewGroup.layoutAnimation = mLayoutAnimationController
        mViewGroup.scheduleLayoutAnimation()
    }

    /**
     * 从底部往上移动动画
     */
    fun AnimationBottomTop(mViewGroup: ViewGroup){
        val mLayoutAnimationController = AnimationUtils.loadLayoutAnimation(mViewGroup.context, R.anim.layout_animation_from_bottom)
        mViewGroup.layoutAnimation = mLayoutAnimationController
        mViewGroup.scheduleLayoutAnimation()
    }

    /**
     * 从右往左移动并透明度逐渐增加
     */
    fun AnimationRightLeft(mViewGroup: ViewGroup){
        val mLayoutAnimationController = AnimationUtils.loadLayoutAnimation(mViewGroup.context, R.anim.layout_animation_from_right)
        mViewGroup.layoutAnimation = mLayoutAnimationController
        mViewGroup.scheduleLayoutAnimation()
    }

    fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context: Context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_top)
        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }
}