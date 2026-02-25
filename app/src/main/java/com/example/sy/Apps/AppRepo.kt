package com.example.sy.Apps

import com.example.lib_base.ApiService
import com.example.lib_base.BaseRepo
import com.example.lib_base.Entity.AppsEntity
import com.example.lib_base.Res
import com.example.lib_http.HttpType
import com.example.lib_http.NetWorkFac
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppRepo @Inject constructor():BaseRepo(){
    private val apiService by lazy {
        NetWorkFac.factory(HttpType.None).create(ApiService::class.java)
    }

    fun getapps():Flow<Res<AppsEntity>>{
        return apiService.getAllApps()
    }
}