package com.example.lib_http

import com.example.lib_http.HttpType.*
import retrofit2.Retrofit

object NetWorkFac {
    lateinit var retrofit: Retrofit
    fun factory(type: HttpType):Retrofit{
        retrofit = when(type){
            None -> NoneRetroFit().createRetrofit()
            Token -> TokenRetroFit().createRetrofit()
            Video -> VideoTokenRetroFit().createRetrofit()
        }
        return retrofit
    }
}