package com.easy.lib_ui.fragment

import android.view.View
import android.view.ViewStub
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easy.lib_ui.R
import com.easy.lib_ui.activity.BaseActivity
import com.easy.lib_ui.adapter.BaseDataBindAdapter
import com.easy.lib_ui.mvvm.model.BaseModel
import com.easy.lib_ui.mvvm.viewmodel.BaseViewModel
import com.easy.lib_util.animation.BaseAnimationUtil
import com.easy.lib_util.executor.AppExecutorsHelper
import com.easy.lib_util.ext.no
import com.easy.lib_util.ext.observe
import com.easy.lib_util.ext.yes
import com.easy.lib_util.toast.toast
import com.scwang.smart.refresh.layout.SmartRefreshLayout

abstract class BaseRefreshFragment <VDB : ViewDataBinding, BRVM : BaseViewModel,IDB: ViewDataBinding,T>(
    val layout: Int,
    val itemLayout: Int,
    val brId: Int
) : BaseFragment<VDB, BRVM>(layout, brId){

    val mAdapter by lazy { RefreshAdapter() }
    override fun initData() {
        initRefreshView()
    }

    open val isAnimation:Boolean get() = true

    //空数据显示
    private var mEmptyView: ViewStub? = null
    private fun showNoDataView(show: Boolean) {
        (mEmptyView == null).yes {
            mEmptyView = onBindEmptyView()
            mEmptyView?.layoutResource = R.layout.view_no_data
            mEmptyView?.inflate()
        }

        mEmptyView?.visibility = if (show) View.VISIBLE else View.GONE

        onBindRecyclerView().visibility = if (show) View.GONE else View.VISIBLE
    }

    //初始化刷新相关
    private fun initRefreshView() {
        onBindRecyclerView().adapter = mAdapter
        onBindRecyclerView().layoutManager = onLayoutManager()

        // 下拉刷新
        onBindRefreshLayout().setOnRefreshListener{
            mAdapter.clear()
            mViewModel.refreshData()
        }
        // 上拉加载
        onBindRefreshLayout().setOnLoadMoreListener{
            //这里做了10秒延时操作,用作处理数据返回异常  造成加载动画一直存在的问题
            var oldSize = mAdapter.listData.size
            mViewModel.loadMore()
            AppExecutorsHelper.postDelayed({
                if (oldSize == mAdapter.listData.size){
                    stopRefreshLoad(false)
                    "数据加载失败".toast()
                }
            },10)
        }

        //自动执行刷新操作
//        onBindRefreshLayout().autoRefresh()
        firstLoad()
    }

    open fun firstLoad(){
        mAdapter.clear()
        mViewModel.refreshData()
    }

    //布局管理器
    open fun onLayoutManager() = LinearLayoutManager(mContext)

    fun stopRefreshLoad(isLoadMore:Boolean = false){
        (mViewModel.isRefresh.value!!).yes {
            onBindRefreshLayout().apply {
                //结束刷新动画
                finishRefresh()
                //设置是否可以加载下一页
                setEnableLoadMore(isLoadMore)
            }
        }.no {
            onBindRefreshLayout().apply {
                //结束加载动画
                finishLoadMore()
                //设置是否可以加载下一页
                setEnableLoadMore(isLoadMore)
            }
        }
    }

    //获取刷新的控件
    abstract fun onBindRefreshLayout(): SmartRefreshLayout

    /**
     * 获取RecyclerView  骨架布局与数据加载逻辑使用
     */
    abstract fun onBindRecyclerView(): RecyclerView

    /**
     * 获取没有数据时显示的控件  骨架布局与数据加载逻辑使用
     */
    abstract fun onBindEmptyView(): ViewStub

    /**
     * 如果不需要分页  在执行这个方法之前 设置 mViewModel.mPage.value = -1
     */
    open fun load(list:MutableList<T> ? = null){

        if (mViewModel.isRefresh.value!!){
            mAdapter.clear()
        }

        if (list == null){
            if (mViewModel.mPage.value == 1){
                showNoDataView(true)
            }

            stopRefreshLoad(false)
            return
        }

        showNoDataView(false)

        if (isAnimation){
            BaseAnimationUtil.AnimationTopBottom(onBindRecyclerView())
        }

        if (mViewModel.mPage.value!! < 0){//不需要分页  直接加载数据
            mAdapter.setNewList(list)
            stopRefreshLoad(false)
        }else{//分页加载
            when(mAdapter.listData.size){
                0->{
                    mAdapter.setNewList(list)
                }
                else ->{
                    mAdapter.addList(list)
                }
            }

            stopRefreshLoad(mAdapter.listData.size == (mViewModel.mLimit.value!! * mViewModel.mPage.value!!))
        }


    }

    inner class RefreshAdapter : BaseDataBindAdapter<T, IDB>(){
        override fun onBindLayout(viewType: Int): Int = itemLayout

        override fun onBindItem(binding: IDB, item: T, position: Int) {
            onAdapterItem(binding, item, position)
        }
    }

    abstract fun onAdapterItem(mAdapterBinding: IDB, item: T, position: Int)

    override fun initViewObservable() {
        super.initViewObservable()
        observe(mViewModel.uiStopRefresh,{
            if (onBindRefreshLayout().isRefreshing){
                onBindRefreshLayout().finishRefresh()
            }

            if (onBindRefreshLayout().isLoading){
                onBindRefreshLayout().finishLoadMore()
            }
        })
    }
}