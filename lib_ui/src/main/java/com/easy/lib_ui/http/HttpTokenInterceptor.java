package com.easy.lib_ui.http;

import android.util.Log;

import com.easy.lib_util.store.BaseConstants;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 公司：~漫漫人生路~总得错几步~
 * 作者：Android 孟从伦
 * 文件名：HttpHeaderInterceptor
 * 创建时间：2020/7/14
 * 功能描述： 带有请求头的拦截器
 */
public class HttpTokenInterceptor implements Interceptor {
    private String TAG = "Retrofit";
    private boolean isDeBug = true;

    @Override
    public Response intercept(Chain chain) throws IOException {

        //  配置请求头
        String tokenType = BaseConstants.tokenKey;
        String token = "";
        if (BaseConstants.INSTANCE.getTokenValue() == null || BaseConstants.INSTANCE.getTokenValue().equals(tokenType)) {
            token = "";
        } else token = BaseConstants.INSTANCE.getTokenValue();

        Request request = chain.request().newBuilder()
                .header(tokenType, token)
                .header("server", "nginx/1.15.11")
                .header("Content-Type", "application/json; charset=utf-8")
                .addHeader("Connection", "close")
                .addHeader("Accept-Encoding", "chunked")
                .build();
        Response response = chain.proceed(request);
        if (isDeBug) {
            Log.d(TAG, String.format("LogUtil---->Retrofit\n||请求链接：%s\n||请求Method:%s\n||请求token：%s\n||请求参数：%s\n||请求响应:%s", response.toString(), request.method(), request.headers().get(tokenType), getRequestInfo(request), getResponseInfo(response)));
        }

        return response;

    }


    /**
     * 打印请求消息
     *
     * @param request 请求的对象
     */
    private String getRequestInfo(Request request) {
        String str = "";
        if (request == null) {
            return str;
        }
        RequestBody requestBody = request.body();
        if (requestBody == null) {
            return str;
        }
        try {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            //将返回的数据URLDecoder解码
            str = buffer.readUtf8();
            str = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            str = URLDecoder.decode(str, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 打印返回消息
     *
     * @param response 返回的对象
     */
    private String getResponseInfo(Response response) {
        String str = "";
        if (response == null || !response.isSuccessful()) {
            return str;
        }
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();
        Charset charset = Charset.forName("utf-8");
        if (contentLength != 0) {
            str = buffer.clone().readString(charset);
        }
        return str;
    }

}

