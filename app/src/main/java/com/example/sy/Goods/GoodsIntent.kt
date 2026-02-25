package com.example.sy.Goods

import com.example.lib_base.UiIntent
import retrofit2.http.Query

sealed class GoodsIntent:UiIntent {
    data class goods(
        var category_id:Int,
        var currentPage:Int,
        var pageSize:Int
    ):GoodsIntent()

    data class goodsXq(var goods_id:Int):GoodsIntent()

    data class addGoods(var map: Map<String,Any>):GoodsIntent()

    data object carList : GoodsIntent()

}