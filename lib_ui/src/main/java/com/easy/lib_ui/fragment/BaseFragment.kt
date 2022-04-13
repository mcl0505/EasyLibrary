package com.easy.lib_ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.easy.lib_ui.R
import com.easy.lib_ui.TitleBar
import com.easy.lib_ui.mvvm.model.BaseModel
import com.easy.lib_ui.mvvm.viewmodel.BaseViewModel
import com.easy.lib_util.ext.visibleOrGone
import com.kckj.baselibrary.ext.bindingInflate

abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes private val layoutId: Int,
    private val varViewModelId: Int? = null,
    sharedViewModel: Boolean = false
) : EasyFragment<V, VM>(sharedViewModel) {

    final override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V = bindingInflate(inflater,layoutId,container)

    final override fun initViewAndViewModel() {
        super.initViewAndViewModel()
        // 绑定 v 和 vm
        if (varViewModelId != null) {
            mBinding.setVariable(varViewModelId, mViewModel)
        }

        // 让 LiveData 和 xml 可以双向绑定
        mBinding.lifecycleOwner = this
    }

    override fun onDestroyView() {
        mBinding.unbind()
        super.onDestroyView()
    }
}