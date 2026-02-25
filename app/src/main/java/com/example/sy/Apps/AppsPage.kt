package com.example.sy.Apps

import android.annotation.SuppressLint
import android.os.Environment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.BaseApp
import com.example.lib_base.BaseLoadingDialog
import com.example.lib_base.BaseTitle
import com.example.lib_base.Entity.AppsEntity
import com.example.lib_base.Entity.AppsEntityItem
import com.example.lib_base.StateType
import com.example.lib_base.UiStare
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AppsPage(vm:AppViewModel = hiltViewModel(),navHostController: NavHostController) {
    val list = remember {
        SnapshotStateList<AppsEntityItem>()
    }
    var isShowProgress by remember {
        mutableStateOf(false)
    }
    var isShowLaunch by remember {
        mutableStateOf(false)
    }
    var currentProgress by remember {
        mutableStateOf(0f)
    }

// 使用 rememberCoroutineScope 记住协程作用域，这是一个在组合中使用的协程作用域
// 当组合发生变化时，它会自动取消，从而避免在组合不再存在时启动协程
// 它通常用于在 UI 事件处理程序中启动协程
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = "") {
        launch {
            vm.uiStare.collect{
                when(it){
                    is UiStare.onFail -> {

                    }
                    UiStare.onLoad -> {

                    }
                    is UiStare.onSuccess<*> -> {
                        if (it.stateType== StateType.DEFAULT) {
                            isShowProgress = false
                            list.clear()
                            list.addAll(it.data as AppsEntity)
                        }
                        if (it.stateType== StateType.DOWNLOAD){
                            ToastUtils.showLong("下载成功")
                        }
                    }
                }
            }
        }
        vm.sendIntent(AppIntent.apps)
    }


    if (isShowProgress){
        BaseLoadingDialog()
    }

    Column {
        BaseTitle("应用", navHostController = navHostController)


        LazyVerticalGrid(columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            items(list){
                AppsItem(it,vm){
                    isShowLaunch=true
// 使用协程作用域启动一个协程
                    scope.launch {
    // 循环从1到100，模拟进度更新
                        for (i in 1 .. 100){
        // 计算当前进度值，范围从0.01到1.0
                            currentProgress=i.toFloat()/100
        // 延迟100毫秒，模拟进度更新间隔
                            delay(100)
                        }
    // 进度完成后，隐藏启动界面
                        isShowLaunch=false
    // 显示安装成功的提示信息
                        ToastUtils.showLong("安装成功")
                    }
                }
            }
        }

    }

    if (isShowLaunch){
        AlertDialog(onDismissRequest = {}, confirmButton = {},
            text = {
                Column (verticalArrangement = Arrangement.spacedBy(10.dp)){
                    LinearProgressIndicator(progress = currentProgress)
                    CircularProgressIndicator(currentProgress)
                    Text(text = "${(currentProgress*100).toInt()}%")
                    Text(text = "${(currentProgress*100).toInt()}/100")
                }
            })
    }



}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppsItem(it: AppsEntityItem, vm: AppViewModel, onlongclick: () -> Unit) {
    //系统路劲
    // val file=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    var file= BaseApp.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    var isshowdownload by remember {
        mutableStateOf(false)
    }
    Card (modifier = Modifier.padding(10.dp).clickable { isshowdownload=true }, colors = CardDefaults.cardColors(
        containerColor = Color.White
    ), border = BorderStroke(1.dp, Color.Gray), elevation = CardDefaults.cardElevation(9.dp)
    ){
        Column (modifier = Modifier.padding(10.dp).combinedClickable (
            onClick = {
                isshowdownload=true
            }, onLongClick = {
                onlongclick()
            }
        ), verticalArrangement = Arrangement.spacedBy(10.dp)){
            AsyncImage(model = it.appIcon, contentDescription = "",
                modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally))
            Text("名称：${it.appName}")
            Text("网址：${it.apkUrl}", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
    if (isshowdownload){
        AlertDialog(onDismissRequest = {isshowdownload=false}, confirmButton = {
            TextButton(onClick = {
                isshowdownload=false
                //发送下载意图
                vm.sendIntent(AppIntent.download(
                    it.netUrl,
                    File(file,it.appName+".apk")
                ))
            }) {
                Text("确认")
            }
        }, dismissButton = {
            TextButton(onClick = {
                isshowdownload=false
            }) { Text("取消") }
        }, title = { Text("下载提醒") },
            text = { Text("是否下载${it.appName}") })
    }
}



