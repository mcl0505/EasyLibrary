package com.easy.lib_ui.mvvm.model

import com.alibaba.fastjson.JSON
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

/**
 * Model 层的基类  数据获取成
 */
abstract class BaseModel : IModel {
    override fun onCleared() {}


    open fun postJson(map: Map<String, String>): RequestBody = RequestBody.create(
        "application/json;charset=UTF-8".toMediaTypeOrNull(),
        JSON.toJSONString(map)
    )

    open fun postJson2(map: Map<String, Any>): RequestBody = RequestBody.create(
        "application/json;charset=UTF-8".toMediaTypeOrNull(),
        JSON.toJSONString(map)
    )

    open fun postJsonObject(ojb: Any): RequestBody = RequestBody.create(
        "application/json;charset=UTF-8".toMediaTypeOrNull(),
        JSON.toJSONString(ojb)
    )
}