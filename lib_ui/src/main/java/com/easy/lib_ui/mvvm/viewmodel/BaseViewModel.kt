package com.easy.lib_ui.mvvm.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.collection.ArrayMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.lib_ui.http.BaseResult
import com.easy.lib_ui.http.HttpHandler
import com.easy.lib_ui.http.IBaseResponse
import com.easy.lib_ui.http.RepositoryManager
import com.easy.lib_ui.http.notHttpException
import com.easy.lib_ui.mvvm.SingleLiveEvent
import com.easy.lib_ui.mvvm.TokenInvalidLiveData
import com.easy.lib_ui.mvvm.model.BaseModel
import com.easy.lib_util.LogUtil
import com.easy.lib_util.Utils
import com.easy.lib_util.bus.LiveDataBus

import com.easy.lib_util.toast.toast
import com.imyyq.mvvm.base.IActivityResult
import com.imyyq.mvvm.base.IArgumentsFromBundle
import com.imyyq.mvvm.base.IArgumentsFromIntent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import retrofit2.Call
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*
import javax.security.auth.callback.Callback

open class BaseViewModel() : ViewModel(), IViewModel{
    val onClickListener = View.OnClickListener {
        clickEvent(it)
    }

    open fun clickEvent(view:View){}

    private lateinit var mCallList: MutableList<Call<*>>
    private lateinit var mCoroutineScope: CoroutineScope

    //发起网络请求
    fun launch(block: suspend () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                error(e)
                LogUtil.d(e.message)
            } finally {
            }
        }
    }



    @CallSuper
    override fun onCleared() {
        LiveDataBus.removeObserve(this)
        LiveDataBus.removeStickyObserver(this)
        cancelConsumingTask()
    }

    /**
     * 取消耗时任务，比如在界面销毁时，或者在对话框消失时
     */
    fun cancelConsumingTask() {
        // ViewModel销毁时会执行，同时取消所有异步任务
        if (this::mCallList.isInitialized) {
            mCallList.forEach { it.cancel() }
            mCallList.clear()
        }
        if (this::mCoroutineScope.isInitialized) {
            mCoroutineScope.cancel()
        }
    }

    // ===================================================================================
    /**
     * 列表数据
     */
    var mPage = MutableLiveData<Int>(-1)
    var mLimit = MutableLiveData<Int>(20)
    var isRefresh = MutableLiveData(true)
    var uiStopRefresh = MutableLiveData(false)
    open fun refreshData() {
        isRefresh.value = true
        mPage.value = 1
    }

    open fun loadMore() {
        isRefresh.value = false
        mPage.value = mPage.value!! + 1
    }
}