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
import com.easy.lib_ui.http.HttpHandler
import com.easy.lib_ui.http.IBaseResponse
import com.easy.lib_ui.http.RepositoryManager
import com.easy.lib_ui.http.notHttpException
import com.easy.lib_ui.mvvm.SingleLiveEvent
import com.easy.lib_ui.mvvm.TokenInvalidLiveData
import com.easy.lib_ui.mvvm.model.BaseModel
import com.easy.lib_util.Utils
import com.easy.lib_util.bus.LiveDataBus

import com.easy.lib_util.toast.toast
import com.imyyq.mvvm.base.IActivityResult
import com.imyyq.mvvm.base.IArgumentsFromBundle
import com.imyyq.mvvm.base.IArgumentsFromIntent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import retrofit2.Call
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*
import javax.security.auth.callback.Callback

open class BaseViewModel<M : BaseModel>(app: Application) : AndroidViewModel(app), IViewModel,
    IActivityResult, IArgumentsFromBundle, IArgumentsFromIntent {
    /**
     * 可能存在没有仓库的 vm，但我们这里也不要是可 null 的。
     * 如果 vm 没有提供仓库，说明此变量不可用，还去使用的话自然就报错。
     */
    lateinit var mModel: M

    /**
     * 是否自动创建仓库，默认是 true，
     */
    private var isAutoCreateRepo = true

    /**
     * 是否缓存自动创建的仓库，默认是 true
     */
    protected open fun isCacheRepo() = true

    val onClickListener = View.OnClickListener {
        clickEvent(it)
    }

    open fun clickEvent(view:View){}

    private lateinit var mCompositeDisposable: Any
    private lateinit var mCallList: MutableList<Call<*>>
    private lateinit var mCoroutineScope: CoroutineScope

    internal val mUiChangeLiveData by lazy { UiChangeLiveData() }

    internal var mBundle: Bundle? = null
    internal var mIntent: Intent? = null

    constructor(app: Application, model: M) : this(app) {
        isAutoCreateRepo = false
        mModel = model
    }

    /**
     * 所有网络请求都在 mCoroutineScope 域中启动协程，当页面销毁时会自动取消
     */
    fun <T> launch(
        block: suspend CoroutineScope.() -> IBaseResponse<T?>?,
        onSuccess: (() -> Unit)? = null,
        onResult: ((t: T) -> Unit) = {},
        onFailed: ((code: Int, msg: String?) -> Unit)? = {code,msg->
            when(code){
                HttpHandler.CODE_TOKEN_INVALID->{
                    startLoginActivity()
                }
                notHttpException->{
//                    "返回数据格式错误,请重新请求".toast()
                }
                else ->{
                    msg?.let {
                        it.toast()
                    }
                }
            }
        },
        onComplete: (() -> Unit)? = null,
    ) {
        initCoroutineScope()
        mCoroutineScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    block()
                }.let {
                    HttpHandler.handleResult(it, onSuccess, onResult, onFailed)
                }
            } catch (e: Exception) {
                onFailed?.let { HttpHandler.handleException(e, it) }
            } finally {
                onComplete?.invoke()
            }
        }
    }

    private fun initCoroutineScope() {
        if (!this::mCoroutineScope.isInitialized) {
            mCoroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        }
    }

    @CallSuper
    override fun onCreate(owner: LifecycleOwner) {
        if (isAutoCreateRepo) {
            if (!this::mModel.isInitialized) {
                val modelClass: Class<M>?
                val type: Type? = javaClass.genericSuperclass
                modelClass = if (type is ParameterizedType) {
                    @Suppress("UNCHECKED_CAST")
                    type.actualTypeArguments[0] as? Class<M>
                } else null
                if (modelClass != null && modelClass != BaseModel::class.java) {
                    mModel = RepositoryManager.getRepository(modelClass, isCacheRepo())
                }
            }
        }
    }

    @CallSuper
    override fun onCleared() {
        // 可能 mModel 是未初始化的
        if (this::mModel.isInitialized) {
            mModel.onCleared()
        }

        LiveDataBus.removeObserve(this)
        LiveDataBus.removeStickyObserver(this)
        cancelConsumingTask()
    }

    /**
     * 取消耗时任务，比如在界面销毁时，或者在对话框消失时
     */
    fun cancelConsumingTask() {
        // ViewModel销毁时会执行，同时取消所有异步任务
        if (this::mCompositeDisposable.isInitialized) {
            (mCompositeDisposable as CompositeDisposable).clear()
        }
        if (this::mCallList.isInitialized) {
            mCallList.forEach { it.cancel() }
            mCallList.clear()
        }
        if (this::mCoroutineScope.isInitialized) {
            mCoroutineScope.cancel()
        }
    }

    // 以下是界面开启和结束相关的 =========================================================

    fun setResult(resultCode: Int, map: MutableMap<String, *>? = null, bundle: Bundle? = null, ) {
        setResult(resultCode, Utils.getIntentByMapOrBundle(map = map, bundle = bundle))
    }

    fun setResult(resultCode: Int, data: Intent? = null) {
        LiveDataBus.send(mUiChangeLiveData.setResultEvent!!, Pair(resultCode, data))
    }

    open fun finish(resultCode: Int? = null, map: MutableMap<String, *>? = null, bundle: Bundle? = null, ) {
        finish(resultCode, Utils.getIntentByMapOrBundle(map = map, bundle = bundle))
    }

    fun finish(resultCode: Int? = null, data: Intent? = null) {
        LiveDataBus.send(mUiChangeLiveData.finishEvent!!, Pair(resultCode, data))
    }

    fun startLoginActivity() {
        //退出登录
        TokenInvalidLiveData.postValue(true)
    }

    fun startActivity(clazz: Class<out Activity>) {
        LiveDataBus.send(mUiChangeLiveData.startActivityEvent!!, clazz)
    }

    fun startActivity(clazz: Class<out Activity>, map: MutableMap<String, *>? = null,) {
        LiveDataBus.send(mUiChangeLiveData.startActivityWithMapEvent!!, Pair(clazz, map))
    }

    fun startActivity(clazz: Class<out Activity>, bundle: Bundle?) {
        LiveDataBus.send(mUiChangeLiveData.startActivityEventWithBundle!!, Pair(clazz, bundle))
    }

    fun startActivityForResult(clazz: Class<out Activity>) {
        LiveDataBus.send(mUiChangeLiveData.startActivityForResultEvent!!, clazz)
    }

    fun startActivityForResult(clazz: Class<out Activity>, bundle: Bundle?) {
        LiveDataBus.send(mUiChangeLiveData.startActivityForResultEventWithBundle!!,
            Pair(clazz, bundle))
    }

    fun startActivityForResult(clazz: Class<out Activity>, map: ArrayMap<String, *>) {
        LiveDataBus.send(mUiChangeLiveData.startActivityForResultEventWithMap!!, Pair(clazz, map))
    }


    // ===================================================================================

    /**
     * 通用的 Ui 改变变量
     */
    class UiChangeLiveData {
        var showLoadingDialogEvent: SingleLiveEvent<String?>? = null
        var dismissLoadingDialogEvent: SingleLiveEvent<Any?>? = null

        var showCusDialogFragmentEvent: SingleLiveEvent<String>? = null
        var dismissCusDialogFragmentEvent: SingleLiveEvent<Any?>? = null


        var startLoginActivityEvent: String? = null
        var startActivityEvent: String? = null
        var startActivityWithMapEvent: String? = null
        var startActivityEventWithBundle: String? = null

        var startActivityForResultEvent: String? = null
        var startActivityForResultEventWithMap: String? = null
        var startActivityForResultEventWithBundle: String? = null

        var finishEvent: String? = null
        var setResultEvent: String? = null

        var loadSirEvent: SingleLiveEvent<Class<out Callback>?>? = null

        fun initLoadSirEvent() {
            loadSirEvent = SingleLiveEvent()
        }

        fun initLoadingDialogEvent() {
            showLoadingDialogEvent = SingleLiveEvent()
            dismissLoadingDialogEvent = SingleLiveEvent()
        }

        fun initCusDialogFragmentEvent() {
            showCusDialogFragmentEvent = SingleLiveEvent()
            dismissCusDialogFragmentEvent = SingleLiveEvent()
        }

        fun initStartActivityForResultEvent() {
            startActivityForResultEvent = UUID.randomUUID().toString()
            startActivityForResultEventWithMap = UUID.randomUUID().toString()
            startActivityForResultEventWithBundle = UUID.randomUUID().toString()
        }

        fun initStartAndFinishEvent() {
            startLoginActivityEvent = "token"
            startActivityEvent = UUID.randomUUID().toString()
            startActivityWithMapEvent = UUID.randomUUID().toString()
            startActivityEventWithBundle = UUID.randomUUID().toString()
            finishEvent = UUID.randomUUID().toString()
            setResultEvent = UUID.randomUUID().toString()
        }
    }

    override fun getBundle(): Bundle? = mBundle

    override fun getArgumentsIntent(): Intent? = mIntent


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