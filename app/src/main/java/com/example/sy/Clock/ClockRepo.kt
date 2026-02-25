package com.example.sy.Clock

import com.example.lib_base.ApiService
import com.example.lib_base.Entity.ClockEntity
import com.example.lib_base.Res
import com.example.lib_http.BaseRepo
import com.example.lib_http.HttpType
import com.example.lib_http.NetWorkFac
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClockRepo @Inject constructor() : BaseRepo() {
    private val apiService by lazy {
        NetWorkFac.factory(HttpType.Token).create(ApiService::class.java)
    }

    fun clockList():Flow<Res<ClockEntity>>{
        return apiService.clockList()
    }
    fun addClock(map: Map<String,Any>):Flow<Res<Any>>{
        return  apiService.addClock(toBody(map))
    }

}