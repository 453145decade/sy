package com.example.sy.Goods

import com.example.lib_base.ApiService
import com.example.lib_base.Entity.GoodsEntity
import com.example.lib_base.Entity.GoodsXQEntity
import com.example.lib_base.Res
import com.example.lib_base.Entity.CarEntity
import com.example.lib_http.BaseRepo
import com.example.lib_http.HttpType
import com.example.lib_http.NetWorkFac
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GoodsRepo @Inject constructor():BaseRepo() {
    val apiService by lazy {
        NetWorkFac.factory(HttpType.Video).create(ApiService::class.java)
    }

    fun goods(
        category_id:Int,
         currentPage:Int,
         pageSize:Int
    ):Flow<Res<GoodsEntity>>{
        return apiService.goods(category_id, currentPage, pageSize)
    }

    fun goodsXq(goods_id:Int):Flow<Res<GoodsXQEntity>>{
        return apiService.goodsXq(goods_id)
    }

    fun addCar(map: Map<String,Any>):Flow<Res<Any>>{
        return apiService.addCar(toBody(map))
    }

    fun carList():Flow<Res<CarEntity>>{
        return apiService.carList()
    }

}