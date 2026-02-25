package com.example.sy.WorkLog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.BaseLoadingDialog
import com.example.lib_base.BaseTitle
import com.example.lib_base.Const
import com.example.lib_base.Entity.WorkLogEntity
import com.example.lib_base.Entity.WorkLogEntityItem
import com.example.lib_base.StateType
import com.example.lib_base.UiStare
import kotlinx.coroutines.launch

data class WorkLogType(val name:String,val title:String)
@Composable
fun WorkLogPage(modifier: Modifier = Modifier,navHostController: NavHostController,vm:WorkLogViewModel = hiltViewModel()) {
    val list = remember {
        SnapshotStateList<WorkLogEntityItem>()
    }
    var isShowProgress by remember {
        mutableStateOf(false)
    }

    var isShowDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect("") {
        launch {
            vm.uiStare.collect{
                when(it){
                    is UiStare.onFail -> {
                        isShowProgress = false
                        //todo 处理错误
                        ToastUtils.showLong(it.mag)
                    }
                    UiStare.onLoad -> {
                        isShowProgress = true
                    }
                    is UiStare.onSuccess<*> -> {
                        if (it.stateType==StateType.DEFAULT){
                            isShowProgress = false
                            list.clear()
                            list.addAll((it.data as WorkLogEntity).sortedByDescending {
                                it.createTime
                            })
                        }
                        if (it.stateType==StateType.INSERT){
                            ToastUtils.showLong("收藏成功")
                        }
                    }
                }
            }
        }
        vm.sendIntent(WorkLogIntent.workLogList)
    }

    if (isShowProgress){
        BaseLoadingDialog()
    }

    Scaffold (floatingActionButton = {
        FloatingActionButton( onClick = {
            isShowDialog = true
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    }){
        Column (modifier = Modifier
            .padding(it)
            .fillMaxWidth()
            .padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){

            BaseTitle(title = "工作日志", navHostController = navHostController)
            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                 items(list){
                     workLogItem(it, vm = vm)
                 }
            }
            Button(onClick = {
                navHostController.navigate(Const.ScWorkLogPage)
            }) {
                Text("前往收藏界面")
            }
        }
    }

    if (isShowDialog){
        val list = listOf(
            WorkLogType("1","日报"),
            WorkLogType("7","周报"),
            WorkLogType("30","月报"),
            WorkLogType("季度","季度报表"),
            WorkLogType("业绩","业绩报表"),
            WorkLogType("营业","营业报表"),
        )

        AlertDialog(onDismissRequest = {isShowDialog =false}, confirmButton = {}, text = {
            LazyVerticalGrid(columns = GridCells.Fixed(3), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(list){
                    worklogtype(it){
                        isShowDialog = false
                         navHostController.navigate(Const.AddWorkLogPage+"/${it.title}")
                    }
                }
            }
        })
    }

}

@Composable
fun worklogtype(it: WorkLogType,onclick:()->Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
        onclick()
    }) {
        Column (modifier = Modifier
            .size(50.dp)
            .background(Color.LightGray, CircleShape)
            .padding(5.dp)
            .border(
                1.dp,Color.Blue,
                RoundedCornerShape(10.dp)
            ).background(Color.White, RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(it.name)
        }

        Text(it.title)
    }
}

@Composable
fun workLogItem(it: WorkLogEntityItem,vm:WorkLogViewModel) {
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clickable {
            vm.sendIntent(WorkLogIntent.StoreReport(it))
        },
        verticalArrangement = Arrangement.spacedBy(10.dp)){

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AsyncImage(model = "https://ts1.tc.mm.bing.net/th/id/OIP-C.qlrFPRRxXv2grh24Lz_lkQHaE-?rs=1&pid=ImgDetMain&o=7&rm=3","", modifier = Modifier
                .size(40.dp)
                .clip(
                    CircleShape
                ))
            Column(modifier = Modifier.height(40.dp), verticalArrangement = Arrangement.SpaceAround) {
                Text("日报")
                Text(it.createTime)
            }
        }

        Text("工作内容 ： ${it.content}")
        Text("日报状态 ： ${it.status}")
        Text("发布人 ： ${it.staffName}")
        Text("发布部门 ： ${it.deptName}")
    }
    Divider(thickness = 0.5.dp)
}
