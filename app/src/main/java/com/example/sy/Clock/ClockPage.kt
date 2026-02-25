package com.example.sy.Clock

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.BaseLoadingDialog
import com.example.lib_base.BaseTitle
import com.example.lib_base.Const
import com.example.lib_base.Entity.ClockEntity
import com.example.lib_base.Entity.ClockEntityItem
import com.example.lib_base.StateType
import com.example.lib_base.UiStare
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Composable
fun ClockPage(navHostController: NavHostController,vm:ClockViewModel = hiltViewModel()) {

    var list = remember {
        SnapshotStateList<ClockEntityItem>()
    }
    var isShowLoad by remember {
        mutableStateOf(false)
    }

    LaunchedEffect("") {
        launch {
            vm.uiStare.collect{
                when(it){
                    is UiStare.onFail -> {
                        ToastUtils.showLong(it.mag)
                        isShowLoad = false
                    }
                    UiStare.onLoad -> {
                        isShowLoad = true
                    }
                    is UiStare.onSuccess<*> -> {
                        when(it.stateType){
                            StateType.DEFAULT -> {
                                isShowLoad = false
                                list.clear()
                                list.addAll(it.data as ClockEntity)
                                Log.d("aaa", "ClockPage: "+list.size, )
                            }
                            StateType.DOWNLOAD -> {

                            }
                            StateType.Scan -> {

                            }
                            StateType.INSERT -> {}
                            StateType.del -> {}
                            else->{}
                        }
                    }
                }
            }
        }
        vm.sendIntent(ClockIntent.clockList)
    }

    if (isShowLoad){
        BaseLoadingDialog()
    }

    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        BaseTitle(title = "考勤打卡", navHostController = navHostController)

        val date= SimpleDateFormat("yyyy-MM-dd").format(/* obj = */ System.currentTimeMillis())
        val week= TimeUtils.getChineseWeek(System.currentTimeMillis())//星期

        Row(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("时间")
            Text(text = week, modifier = Modifier.weight(1f))
            Text("考勤规则")
        }
        LazyColumn (modifier = Modifier.weight(1f).fillMaxSize()){
            items(list){
                clocListkItem(it)
            }
        }
        Button(onClick = {
            navHostController.navigate(Const.AddSignPage)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("签到")
        }
    }
}

@Composable
fun clocListkItem(it: ClockEntityItem) {
    Card (modifier = Modifier.fillMaxSize().padding(10.dp), colors = CardDefaults.cardColors(
// 设置容器颜色为白色
        containerColor = Color.White
    ), border = BorderStroke(1.dp, Color.Gray), elevation = CardDefaults.cardElevation(9.dp)
    ){
        Column(modifier = Modifier.padding(10.dp)) {
            Text("签到信息")
            Text("经纬度", fontSize = 10.sp, color = Color.LightGray)
            Text("经度： "+it.lon+"纬度： "+it.lat,fontSize = 10.sp, color = Color.LightGray)
            Text("打卡类型 : "+it.type)
            Text("打卡状态 : "+it.status)
            Text("打卡地点 : "+it.address)
            Text("打卡时间 : "+it.createTime)
        }


    }
}
