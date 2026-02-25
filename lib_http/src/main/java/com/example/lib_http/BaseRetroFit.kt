package com.example.lib_http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

abstract class BaseRetroFit:HttpRetroFit {
    fun createHttp():OkHttpClient.Builder{
        return OkHttpClient.Builder()
            .writeTimeout(100,TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS)
            .connectTimeout(100,TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }
}