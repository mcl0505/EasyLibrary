package com.easy.lib_ui.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.easy.lib_ui.mvvm.model.BaseModel
import com.easy.lib_ui.mvvm.viewmodel.BaseViewModel
import com.easy.lib_util.LogUtil
import com.kckj.baselibrary.ext.bindingInflate

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<out BaseModel>>(
    @LayoutRes
    private val layoutId: Int,
    private val varViewModelId: Int,
) : EasyActivity<V, VM>() {

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V =
        bindingInflate(inflater, layoutId, container)


    @SuppressLint("MissingSuperCall")
    final override fun initViewAndViewModel() {
        super.initViewAndViewModel()
//        // 绑定 v 和 vm
        if (varViewModelId != null) {
            mBinding.setVariable(varViewModelId, mViewModel)
        }
        // 让 LiveData 和 xml 可以双向绑定
        mBinding.lifecycleOwner = this
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }
}