package com.example.sy.Goods

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.lib_base.Const
import com.example.lib_base.Entity.GoodsEntity
import com.example.lib_base.Entity.GoodsEntityItem
import com.example.lib_base.StateType
import com.example.lib_base.UiStare
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun GoodsPage(navHostController: NavHostController,vm:GoodsViewModel = hiltViewModel()) {
    var list = remember {
        mutableStateListOf<GoodsEntityItem>()

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
                                list.clear()
                                list.addAll(it.data as GoodsEntity)
                            }
                            StateType.DOWNLOAD -> {

                            }
                            StateType.Scan -> {}
                            StateType.INSERT -> {}
                            StateType.del -> {}
                            StateType.comment -> {}
                            StateType.send -> {}
                        }
                    }
                }
            }
        }
        vm.sendIntent(GoodsIntent.goods(0,2,10))
    }


    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        LazyVerticalGrid(columns = GridCells.Fixed(2), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(list){
                goodsItem(it, navHostController = navHostController)
            }
        }
    }


}

@Composable
fun goodsItem(it: GoodsEntityItem,navHostController: NavHostController) {
    Column(modifier = Modifier.fillMaxWidth()
        .clickable {
            val s = Gson().toJson(it)
            MMKV.defaultMMKV().encode("goods",s)
            navHostController.navigate(Const.GoodsXQ)
        }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        AsyncImage(model = it.goods_default_icon,"", modifier = Modifier.size(100.dp))
        Text(text = it.goods_desc, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("原价： "+it.goods_default_price, color = Color.LightGray,
                    textDecoration = TextDecoration.LineThrough)
                Text("原价： "+(it.goods_default_price * 0.8).toInt(), color = Color.Red,)
            }
            Icon(Icons.Default.ShoppingCart,"", modifier = Modifier.size(30.dp), tint = Color.LightGray)
        }
    }
}
