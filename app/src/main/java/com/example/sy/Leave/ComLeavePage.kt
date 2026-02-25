package com.example.sy.Leave

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.lib_base.Const
import com.example.lib_base.Entity.LeaveEntity
import com.example.lib_base.Entity.LeaveEntityItem
import com.example.lib_base.StateType
import com.example.lib_base.StateType.*
import com.example.lib_base.UiStare
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun ComLeavePage(navHostController: NavHostController,vm:LeaveViewModel = hiltViewModel()) {
    val list = remember {
        SnapshotStateList<LeaveEntityItem>()
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
                                list.clear()
                                list.addAll((it.data as LeaveEntity).filter { it.status == "未审核" })
                                Log.d("", "LeavePage: "+list.size)
                            }
                            DOWNLOAD -> {}
                            Scan -> {}
                            INSERT -> {}
                            del -> {}
                            comment -> {}
                            send -> {}
                        }

                    }


                }
            }
        }
        vm.sendIntent(LeaveIntent.leave("0","1500"))
    }


    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(list){
                comLeaveItem(it,navHostController)
            }
        }
    }



}

@Composable
fun comLeaveItem(it: LeaveEntityItem, navHostController: NavHostController) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.LightGray)
        .padding(10.dp)
        .clickable {
            val s = Gson().toJson(it)
            navHostController.navigate(Const.ComLeaveXq+"/${s}")

        }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text( text = if(it.type.isNotEmpty()) it.type else "数据缺失")
        Text( text = if(it.startTime!=null) it.startTime.substring(0,10) else "数据缺失")
        Text( text = if(it.endTime!=null) it.endTime.substring(0,10) else "数据缺失")
        var color = if (it.status == "通过" ) Color.Green else if(it.status == "拒绝") Color.Red else Color.Gray
        Text(it.status, modifier = Modifier.background(color, RoundedCornerShape(10.dp)).padding(5.dp), color = Color.White,)

    }
}
