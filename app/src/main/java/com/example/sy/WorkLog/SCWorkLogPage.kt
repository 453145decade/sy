package com.example.sy.WorkLog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.Entity.WorkLogEntity
import com.example.lib_base.Entity.WorkLogEntityItem
import com.example.lib_base.StateType
import com.example.lib_base.UiStare
import kotlinx.coroutines.launch

@Composable
fun ScWorkLogPage(modifier: Modifier = Modifier,vm:WorkLogViewModel= hiltViewModel()) {
    val list = remember {
        SnapshotStateList<WorkLogEntityItem>()
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
                        if (it.stateType== StateType.DEFAULT){
                            list.clear()
                            list.addAll(it.data as List<WorkLogEntityItem>)
                        }
                        if (it.stateType== StateType.del){
                            ToastUtils.showLong("取消成功")
                            vm.sendIntent(WorkLogIntent.QueryAllReport)
                        }

                    }
                }
            }
        }
        vm.sendIntent(WorkLogIntent.QueryAllReport)
    }

    LazyColumn {
        items(list){
            scWorkLogItem(it)
        }
    }

}

@Composable
fun scWorkLogItem(it: WorkLogEntityItem,vm:WorkLogViewModel = hiltViewModel()) {
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clickable {
            vm.sendIntent(WorkLogIntent.del(it))
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

        Text("工作内容：${it.content}")
        Text("日报状态：${it.status}")
        Text("发布状态：${it.staffName}")
        Text("发布部门：${it.deptName}")
    }
    Divider(thickness = 0.5.dp)
}
