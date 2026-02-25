package com.example.user

import com.example.lib_base.ApiService
import com.example.lib_base.BaseRepo
import com.example.lib_base.Res
import com.example.lib_http.HttpType
import com.example.lib_http.NetWorkFac
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepo @Inject constructor():BaseRepo() {
    // 使用 lazy 委托属性实现延迟初始化，只在第一次访问时进行初始化
    private val apiService by lazy {
        // 通过 NetWorkFac 工厂类创建网络请求服务
        // HttpType.None 表示不使用特定的HTTP类型配置
        // 创建 ApiService 接口的实现类实例
        NetWorkFac.factory(HttpType.None).create(ApiService::class.java)
    }

    fun login(map: Map<String,Any>):Flow<Res<Any>>{
        return apiService.login(toBody(map))
    }
    fun register(map: Map<String,Any>):Flow<Res<Any>>{
        return apiService.register(toBody(map))
    }
}