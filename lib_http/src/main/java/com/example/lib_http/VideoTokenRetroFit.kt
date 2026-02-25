package com.example.lib_http

import com.example.lib_base.Const
import com.zyj.retrofit.adapter.FlowCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoTokenRetroFit : BaseRetroFit() {
    override fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Const.Video_Base_url)
            .client(createHttp()
                .addInterceptor {
                    val request = it.request().newBuilder().addHeader(
                        Const.VideoToken,
                        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsImV4cCI6MTc2NzY2MDM5NCwidXNlcm5hbWUiOiJmYWl6In0.UjamK6wQFb03SZ9q4dQxR_nhiT_JwTrDtO1C-uFHKyc"
                    ).build()
                    it.proceed(request)
                }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .build()
    }

}