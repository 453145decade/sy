package com.example.sy.Leave

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.lib_base.Const
import com.example.lib_base.Entity.LeaveEntity
import com.example.lib_base.Entity.LeaveEntityItem
import com.example.lib_base.StateType
import com.example.lib_base.StateType.*
import com.example.lib_base.UiStare
import com.example.sy.R
import com.google.gson.Gson
import com.king.ultraswiperefresh.UltraSwipeRefresh
import com.king.ultraswiperefresh.rememberUltraSwipeRefreshState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//data class Leave(var )
@Composable
fun LeavePage(vm:LeaveViewModel = hiltViewModel(),navHostController: NavHostController) {
    var list = remember {
        SnapshotStateList<LeaveEntityItem>()
    }

    var page by remember {
        mutableStateOf(0)
    }
    var limit by remember {
        mutableStateOf(10)
    }
// 使用 rememberUltraSwipeRefreshState() 记忆并创建下拉刷新的状态
// 这是一个可组合的状态，用于跟踪下拉刷新组件的状态
    var stare = rememberUltraSwipeRefreshState()

    LaunchedEffect(stare.isRefreshing) {
        if (stare.isRefreshing){
            page = 1
            vm.sendIntent(LeaveIntent.leave(page.toString(),limit.toString()))
            stare.isRefreshing = false
        }
    }
    LaunchedEffect(stare.isLoading) {
        if (stare.isLoading){
            page++
            vm.sendIntent(LeaveIntent.leave(page.toString(),limit.toString()))
            stare.isLoading = false
        }
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
                            DEFAULT -> {
                                if (page == 1){
                                    list.clear()
                                }

                                list.addAll(it.data as LeaveEntity)
                                Log.d("", "LeavePage: "+list.size)
                            }
                            DOWNLOAD -> {}
                            Scan -> {}
                            INSERT -> {}
                            del -> {}
                            else->{}
                        }

                    }
                }
            }
        }

    }
    vm.sendIntent(LeaveIntent.leave(page.toString(),limit.toString()))

    Column(modifier = Modifier.fillMaxSize()
        .padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Image(Icons.Default.ArrowBack,"", modifier = Modifier.clickable {
                navHostController.popBackStack()
            })
            Spacer(modifier = Modifier.weight(1f))
            Text("请假列表")
            Spacer(modifier = Modifier.weight(1f))
            Text("申请", modifier = Modifier
                .background(colorResource(R.color.o), RoundedCornerShape(5.dp))
                .padding(10.dp,5.dp).clickable {
                    navHostController.navigate(Const.AddLeave)
                })
        }
        Divider(thickness = 0.5.dp)

        Row(modifier = Modifier.fillMaxWidth()
            .background(colorResource(R.color.lb)).padding(10.dp)
            ,Arrangement.SpaceBetween) {
            var sort = false

            Text("类型", modifier = Modifier.clickable {
                if (sort){
                    list.sortBy { it.type }
                }else{
                    list.sortByDescending { it.type }
                }
                sort=!sort
            })
            Text("开始时间", modifier = Modifier.clickable {
                if (sort){
                    list.sortBy { it.createTime }
                }else{
                    list.sortByDescending { it.createTime }
                }
                sort=!sort
            })
            Text("结束时间", modifier = Modifier.clickable {
                if (sort){
                    list.sortBy { it.endTime }
                }else{
                    list.sortByDescending { it.endTime }
                }
                sort=!sort
            })
            Text("类型", modifier = Modifier.clickable {
                if (sort){
// 使用sortBy函数对list列表进行排序
// 排序的依据是列表中每个元素的status属性
// sortBy会根据给定的表达式返回的值进行升序排序
                    list.sortBy { it.status }
                }else{
                    list.sortByDescending { it.status }
                }
                sort=!sort
            })
        }

        UltraSwipeRefresh(state = stare, onRefresh = {
            stare.isRefreshing = true
        }, onLoadMore = {
            stare.isLoading = true
        }) {
            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(list){
                    LeaveItem(it,navHostController)
                }
            }
        }


    }

}

@Composable
fun LeaveItem(it: LeaveEntityItem,navHostController: NavHostController) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.LightGray)
        .padding(10.dp)
        .clickable {
            val s = Gson().toJson(it)
            navHostController.navigate(Const.LeaveXq+"/${s}")
        }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text( text = if(it.type.isNotEmpty()) it.type else "数据缺失")
        Text( text = if(it.startTime!=null) it.startTime.substring(0,10) else "数据缺失")
        Text( text = if(it.endTime!=null) it.endTime.substring(0,10) else "数据缺失")
        var color = if (it.status == "通过" ) Color.Green else if(it.status == "拒绝") Color.Red else Color.White
        Text(it.status, modifier = Modifier.background(color, RoundedCornerShape(10.dp)).padding(5.dp), color = Color.White,)

    }
}
