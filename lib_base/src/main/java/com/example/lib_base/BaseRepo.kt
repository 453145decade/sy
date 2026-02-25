package com.example.lib_base

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

open class BaseRepo {
    fun toBody(map:Map<String,Any>):RequestBody{
        val json = Gson().toJson(map)
        val body = RequestBody.create("application/json;charset=utf-8".toMediaTypeOrNull(), json)
        return body
    }
}