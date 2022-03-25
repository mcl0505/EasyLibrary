package com.easy.lib_ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.easy.lib_ui.mvvm.TokenInvalidLiveData
import com.easy.lib_ui.mvvm.model.BaseModel
import com.easy.lib_ui.mvvm.viewmodel.BaseViewModel
import com.easy.lib_util.ext.observe
import com.kckj.baselibrary.ext.bindingInflate

/**
 * 如果你的界面足够简单，不需要 ViewModel，那么可以继承自此类。
 * 此类不可以使用 [mViewModel] 变量，不可使用 vm 相关的方法，
 * 不可使用 LoadingDialog，不可使用 LoadSir
 */
abstract class BaseNoViewModelActivity<V : ViewBinding>(
    @LayoutRes
    private val layoutId: Int,
) :
    EasyActivity<V, BaseViewModel<BaseModel>>() {
    @SuppressLint("MissingSuperCall")

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V =
        bindingInflate(inflater, layoutId, container)

    @SuppressLint("MissingSuperCall")
    final override fun initViewAndViewModel() {
    }


    @SuppressLint("MissingSuperCall")
    final override fun initUiChangeLiveData() {

    }


    final override fun initViewModel(viewModelStoreOwner: ViewModelStoreOwner): BaseViewModel<BaseModel> {
        return super.initViewModel(viewModelStoreOwner)
    }

}