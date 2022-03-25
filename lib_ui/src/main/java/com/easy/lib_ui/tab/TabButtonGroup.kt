package com.easy.lib_ui.tab

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.easy.lib_util.store.BaseConstants
import java.util.ArrayList

/**
 * 公司：
 * 作者：Android 孟从伦
 * 创建时间：2020/12/19
 * 功能描述：
 */

class TabButtonGroup @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private val mList: MutableList<TabButton>
    private lateinit var mViewPager: ViewPager2
    private var mCurPosition: Int
    private val mAnimator: ValueAnimator?
    private var mAnimView: View? = null
    private val mRunnable: Runnable
    override fun onFinishInflate() {
        super.onFinishInflate()
        var i = 0
        val count = childCount
        while (i < count) {
            val v = getChildAt(i)
            v.tag = i
            v.setOnClickListener(this)
            mList.add(v as TabButton)
            i++
        }
    }

    override fun onClick(v: View) {
        val tag = v.tag
        if (tag != null) { //            cancelAnim();
            val position = tag as Int
            if (position == mCurPosition) {
                return
            }
            mList[mCurPosition].setChecked(false)
            val tbn = mList[position]
            tbn.setChecked(true)
            mCurPosition = position
            //            mAnimView = tbn;
//            mAnimator.start();
            selectPosition(mCurPosition)
            postDelayed(mRunnable, 150)
            BaseConstants.lastClickTime = System.currentTimeMillis()
        }
    }

    fun setViewPager(viewPager: ViewPager2) {
        mViewPager = viewPager
    }

    fun cancelAnim() {
        mAnimator?.cancel()
    }

    init {
        mList = ArrayList()
        mCurPosition = 0
        mAnimator = ValueAnimator.ofFloat(0.9f, 1.4f, 1f)
        mAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            val v = animation.animatedValue as Float
            if (mAnimView != null) {
                mAnimView!!.setScaleX(v)
                mAnimView!!.setScaleY(v)
            }
        })
        mAnimator.duration = 300
        mAnimator.interpolator = AccelerateDecelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mAnimView = null
            }
        })
        mRunnable = Runnable {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(mCurPosition, false)
            }
        }
    }

    fun setSelectClick(selectPosition:Int){
        mList[selectPosition].performClick()
    }

    fun selectPosition(selectPosition:Int){}
}