package com.example.lib_http

import com.example.lib_base.Const
import com.zyj.retrofit.adapter.FlowCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NoneRetroFit : BaseRetroFit() {
    override fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Const.Base_url)
            .client(createHttp().build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .build()
    }
}