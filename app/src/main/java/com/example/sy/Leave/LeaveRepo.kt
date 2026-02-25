package com.example.sy.Leave

import com.example.lib_base.ApiService
import com.example.lib_base.BaseRepo
import com.example.lib_base.Entity.LeaveEntity
import com.example.lib_base.Res
import com.example.lib_http.HttpType
import com.example.lib_http.NetWorkFac
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LeaveRepo @Inject constructor() :BaseRepo(){
    val apiService by lazy {
        NetWorkFac.factory(HttpType.Token).create(ApiService::class.java)
    }

    fun leave( page:String, limit:String):Flow<Res<LeaveEntity>>{
        return apiService.leave(page, limit)
    }
    fun addLeave(map: Map<String,Any>):Flow<Res<Any>>{
        return apiService.addLeave(toBody(map))
    }
    fun comLeave(map: Map<String, Any>):Flow<Res<Any>>{
        return apiService.comLeave(toBody(map))
    }
}