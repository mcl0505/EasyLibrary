package com.easy.lib_ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.easy.lib_ui.mvvm.model.BaseModel
import com.easy.lib_ui.mvvm.viewmodel.BaseViewModel
import com.kckj.baselibrary.ext.bindingInflate

abstract class BaseNoViewModelFragment<V : ViewBinding>(
    @LayoutRes
    private val layoutId: Int,
) :
    EasyFragment<V, BaseViewModel>()  {

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V = bindingInflate(inflater,layoutId,container)

    @SuppressLint("MissingSuperCall")
    final override fun initViewAndViewModel() {
    }

    @SuppressLint("MissingSuperCall")
    final override fun initUiChangeLiveData() {
    }


    final override fun initViewModel(viewModelStoreOwner: ViewModelStoreOwner): BaseViewModel {
        return super.initViewModel(viewModelStoreOwner)
    }
}