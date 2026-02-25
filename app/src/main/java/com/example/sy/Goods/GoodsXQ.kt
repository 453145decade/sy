package com.example.sy.Goods

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.BaseTitle
import com.example.lib_base.Entity.GoodsEntityItem
import com.example.lib_base.Entity.GoodsXQEntity
import com.example.lib_base.StateType
import com.example.lib_base.UiStare
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


lateinit var goodsXqItem: GoodsXQEntity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GoodsXQ(navHostController: NavHostController,vm:GoodsViewModel = hiltViewModel()) {
    val s = MMKV.defaultMMKV().decodeString("goods")
    val goods = Gson().fromJson(s, GoodsEntityItem::class.java)

    var isLi by remember {
        mutableStateOf(false)
    }

    LaunchedEffect("") {
        launch {
            vm.uiStare.collect{
                when(it){
                    is UiStare.onFail -> {

                    }
                    UiStare.onLoad -> {

                    }
                    is UiStare.onSuccess<*> -> {
                        when(it.stateType){
                            StateType.DEFAULT -> {
                                goodsXqItem = it.data as GoodsXQEntity
                                isLi = true
                            }
                            StateType.DOWNLOAD -> {

                            }
                            StateType.Scan -> {

                            }
                            StateType.INSERT -> {
                                ToastUtils.showLong("添加成功")
                            }
                            StateType.del -> {

                            }
                            StateType.comment -> {

                            }
                            StateType.send -> {

                            }
                        }
                    }
                }
            }

        }
        vm.sendIntent(GoodsIntent.goodsXq(goods.id))
    }

    if (isLi) {
        Column (modifier = Modifier.fillMaxSize()
            .padding(10.dp)
// 使用verticalScroll修饰符使内容可以垂直滚动
// rememberScrollState()用于记住滚动状态，使滚动位置在重组时得以保持
            .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally){

            BaseTitle(title = "商品详情", navHostController = navHostController)

            var bannerList = listOf(
                goodsXqItem.bannerList[0],
                goodsXqItem.bannerList[1],
                goodsXqItem.bannerList[2]
            )
            val stare = rememberPagerState {
                bannerList.size
            }
            LaunchedEffect("") {
                launch {
                    while (true) {
                        delay(1000)
                        var nextIndex = (stare.currentPage + 1) % stare.pageCount
                        stare.animateScrollToPage(nextIndex)
                    }
                }
            }

            HorizontalPager( state = stare, modifier = Modifier.fillMaxWidth().height(230.dp)) {index->
                AsyncImage(model = bannerList[index],"", modifier = Modifier.fillMaxWidth())
            }

            Text(goodsXqItem.goods_desc)
            Text(text = "打折价格 ： "+(goodsXqItem.goods_default_price*0.8).toInt())
            AsyncImage(model = goodsXqItem.goods_detail_one,"", modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.Crop)
            AsyncImage(model = goodsXqItem.goods_detail_two,"", modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.Crop)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Button(onClick = {

                }) {
                    Text("立即购买")
                }
                Button(onClick = {
                    val map = mapOf(
                        "count" to 1,
                        "goods_id" to goodsXqItem.id,
                    )
                    vm.sendIntent(GoodsIntent.addGoods(map))
                }) {
                    Text("加入购物车")
                }
            }

        }
    }




}