package com.example.sy.Goods

import com.example.lib_base.BaseViewModel
import com.example.lib_base.StateType
import com.example.sy.Video.VideoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoodsViewModel @Inject constructor(var goodsRepo: GoodsRepo):BaseViewModel<GoodsIntent>() {
    override fun handleIntent(it: GoodsIntent) {
        when(it){
            is GoodsIntent.goods -> {
                httpRequest(goodsRepo.goods(it.category_id,it.currentPage,it.pageSize))
            }

            is GoodsIntent.goodsXq -> {
                httpRequest(goodsRepo.goodsXq(it.goods_id), stateType = StateType.DEFAULT)
            }

            is GoodsIntent.addGoods -> {
                 httpRequest( goodsRepo.addCar(it.map), stateType = StateType.INSERT)
            }

            GoodsIntent.carList -> {
                httpRequest(goodsRepo.carList(), stateType = StateType.DEFAULT)
            }
        }
    }
}