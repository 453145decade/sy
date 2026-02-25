package com.example.lib_http

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

open class BaseRepo {
    fun toBody( map: Map<String,Any>): RequestBody {
        val s = Gson().toJson(map)
        val body = RequestBody.create("application/json;charset=utf-8".toMediaTypeOrNull(), s)
        return body
    }
}