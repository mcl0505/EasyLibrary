package com.mh55.module_login.http

import com.easy.lib_ui.http.BaseResult
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 *   公司名称: ~漫漫人生路~总得错几步~
 *   创建作者: Android 孟从伦
 *   创建时间: 2021/10/26
 *   功能描述:  接口 API
 *   常用请求方式
 *
 *   TODO 1: POST  请求   传入json 数据
 *   @POST("xxxx")
 *   suspend fun xxx(@Body body: RequestBody): BaseResult<Any?>?    ====>  api.login(postJson(map))
 *
 *   TODO 2: POST 请求   传入表单数据
 *   @POST("Login/updateIdentity")
 *   @FormUrlEncoded
 *   suspend fun xxx(@FieldMap map: Map<String, String>): BaseResult<Any?>?    ===>   api.loginMob(map)
 *
 *   TODO 3: POST 上传文件
 *   @POST("common/appUpload")
 *   @Multipart
 *   suspend fun xxx(@Part file: MultipartBody.Part): BaseResult<String?>?
 *
 *   val file = File(path)
 *   val fileRQ: RequestBody = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
 *   val part: MultipartBody.Part =
 *   MultipartBody.Part.createFormData("file", file.getName(), fileRQ)
 *   return api.postAppUpload(part)
 *
 *   TODO 4: GET 请求  单个数据
 *   @GET("xxxx")
 *   suspend fun xxx(@Query("types") types:String): BaseResult<UpdateBean?>?
 *
 *   TODO 5: GET 请求  多个数据
 *   @GET("xxxx")
 *   suspend fun xxx(@QueryMap map: MutableMap<String, String>): BaseResult<UpdateBean?>?    ===>   api.xxx(map)
 *
 *
 */
interface LoginService {
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") username: String, @Field("password") password: String):BaseResult<String>
}