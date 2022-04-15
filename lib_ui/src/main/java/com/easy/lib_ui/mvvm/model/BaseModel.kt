package com.easy.lib_ui.mvvm.model

import com.alibaba.fastjson.JSON
import com.easy.lib_ui.http.BaseResult
import com.easy.lib_ui.http.DataState
import com.easy.lib_ui.mvvm.StateLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

/**
 * Model 层的基类  数据获取成
 */
abstract class BaseModel : IModel {
    override fun onCleared() {}

    // TODO: 2022/3/30 传递JSON  数据
    open fun postJson(ojb: Any): RequestBody = RequestBody.create(
        "application/json;charset=UTF-8".toMediaTypeOrNull(),
        JSON.toJSONString(ojb)
    )

    /**
     * 方式二：结合Flow请求数据。携带叶念状态管理
     * 根据Flow的不同请求状态，如onStart、onEmpty、onCompletion等设置baseResp.dataState状态值，
     * 最后通过SingleLiveEvent分发给UI层。
     *
     * @param block api的请求方法
     * @param stateLiveData 每个请求传入相应的LiveData，主要负责网络状态的监听
     */
    suspend fun <T : Any> executeReqWithFlow(
        block: suspend () -> BaseResult<T>,
        stateLiveData: StateLiveData<T>?=null
    ) {
        var result = BaseResult<T>()
        flow {
            val respResult = block.invoke()
            result = respResult
            result.dataState = DataState.STATE_LOADING
            stateLiveData?.postValue(result)
            emit(respResult)
        }
            .flowOn(Dispatchers.IO)
            .onStart {
                result.dataState = DataState.STATE_LOADING
                stateLiveData?.postValue(result)
            }
            .onEmpty {
                result.dataState = DataState.STATE_EMPTY
                stateLiveData?.postValue(result)
            }
            .catch { exception ->
                run {
                    exception.printStackTrace()
                    result.dataState = DataState.STATE_LOADING
                    result.error = exception
                    stateLiveData?.postValue(result)
                }
            }
            .collect {
                stateLiveData?.postValue(result)
            }


    }

    /**
     * 方式一   如果不想使用页面状态监听可以使用这个方法
     * repo 请求数据的公共方法，
     * 在不同状态下先设置 baseResp.dataState的值，最后将dataState 的状态通知给UI
     * @param block api的请求方法
     * @param stateLiveData 每个请求传入相应的LiveData，主要负责网络状态的监听
     */
    suspend fun <T : Any> executeResp(
        block: suspend () -> BaseResult<T>,
        stateLiveData: StateLiveData<T>?=null
    ) {
        var baseResp = BaseResult<T>()
        try {
            baseResp.dataState = DataState.STATE_LOADING
            //开始请求数据
            val invoke = block.invoke()
            //将结果复制给baseResp
            baseResp = invoke
            if (baseResp.isSuccess) {
                //请求成功，判断数据是否为空，
                //因为数据有多种类型，需要自己设置类型进行判断
                if (baseResp.data == null || baseResp.data is List<*> && (baseResp.data as List<*>).size == 0) {
                    //TODO: 数据为空,结构变化时需要修改判空条件
                    baseResp.dataState = DataState.STATE_EMPTY
                } else {
                    //请求成功并且数据为空的情况下，为STATE_SUCCESS
                    baseResp.dataState = DataState.STATE_SUCCESS
                }

            } else {
                //服务器请求错误
                baseResp.dataState = DataState.STATE_FAILED
            }
        } catch (e: Exception) {
            //非后台返回错误，捕获到的异常
            baseResp.dataState = DataState.STATE_ERROR
            baseResp.error = e
        } finally {
            stateLiveData?.postValue(baseResp)
        }
    }

}