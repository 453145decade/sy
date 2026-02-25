package com.example.lib_http

import com.blankj.utilcode.util.SPUtils
import com.example.lib_base.Const
import com.tencent.mmkv.MMKV
import com.zyj.retrofit.adapter.FlowCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenRetroFit:BaseRetroFit() {
    override fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Const.Base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .client(createHttp().addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader(
                        Const.Token,
                        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjMzMjcsImN0aW1lIjoxNzYzNDQ5NjQ1fQ.HAQHQCQlXj_yGI7XSas6HR6dWaNfG2wWbiwxBCqx-ac"
//                        MMKV.defaultMMKV().decodeString(Const.Token).toString()
                    ).build()
                it.proceed(request)
            }.build())
            .build()
    }
}