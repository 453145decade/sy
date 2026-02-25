package com.example.document.Page

import android.os.Environment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.blankj.utilcode.util.ToastUtils
import com.example.document.DocumentIntent
import com.example.document.DocumentViewModel
import com.example.document.R
import com.example.lib_base.BaseApp
import com.example.lib_base.BaseViewModel
import com.example.lib_base.FileEntity
import com.example.lib_base.StateType
import com.example.lib_base.StateType.*
import com.example.lib_base.UiStare
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun DocumentPage(navHostController: NavHostController,vm:DocumentViewModel = hiltViewModel()) {
    var list = remember {
        SnapshotStateList<FileEntity>()
    }
    var isShowPress by remember {
        mutableStateOf(false)
    }
    // 获取应用在设备外部存储空间中的下载目录
    // BaseApp.context 应该是应用程序的上下文引用
    // Environment.DIRECTORY_DOWNLOADS 是Android系统定义的下载目录常量
    val file = BaseApp.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

    var  checkFileName by remember {
        mutableStateOf("")
    }
    var checkFilePath by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = "") {
        launch {
            vm.uiStare.collect{
                when(it){
                    is UiStare.onFail -> {
                        isShowPress = false
                        ToastUtils.showLong("本地文件获取失败: ${it.mag}")
                        Log.e("TAG", "本地文件获取失败: ${it.mag}", )
                    }
                    UiStare.onLoad -> {
                        isShowPress = true
                    }
//                    同袍齐心，砥砺并肩，舍小我于身后，而凝力于心间，方能驱邪黑暗，化飞龙耀于九天
                    is UiStare.onSuccess<*> -> {
                        when(it.stateType){
                            DEFAULT -> {
                                ToastUtils.showLong("本地文件获取成功")
                                isShowPress = false
                                list.clear()
                                list.addAll(it.data as List<FileEntity>)
                                Log.e("TAG", "DocumentPage: ${list.size}", )
                            }
                            DOWNLOAD -> {

                            }
                            Scan -> {
                                ToastUtils.showLong("上传成功")
                            }

                            INSERT ->{

                            }

                            del -> {

                            }else->{}
                        }

                    }
                }
            }
        }

        if (file!=null){
            vm.sendIntent(DocumentIntent.ScanLocalFile(file))
        }

    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp).background(color = Color.White)) {


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Image(imageVector = Icons.Default.ArrowBack,
                "",
                modifier = Modifier.clickable {
                    navHostController.popBackStack()
                })
            Text(text = "请选择上传的文件",)
            Text("上传", modifier = Modifier.clickable {
                if(checkFileName.isNotEmpty() && checkFilePath.isNotEmpty()){
                    vm.sendIntent(DocumentIntent.UpLoadFile(checkFileName,checkFilePath))
                }else{
                    ToastUtils.showLong("请选择文件")
                }
            })

        }


        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(list){
                DocumentItem(it,it.name == checkFileName){
                    checkFileName = it.name
                    checkFilePath = it.absolutePath
                }
            }
        }
    }

}

@Composable
fun DocumentItem(it: FileEntity,isChecked:Boolean,onClick:()->Unit) {
    var isShowCheck by remember {
        mutableStateOf(false)
    }
    Row(modifier = Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        RadioButton(selected = isChecked, onClick = {
            onClick()
        })

        Checkbox(checked = isChecked, onCheckedChange = {
            onClick()
        })
        Box(contentAlignment = Alignment.TopEnd){
            Image(painter = painterResource(R.drawable.document),"", modifier = Modifier.size(60.dp).clickable {
                onClick()
            })
            Image(painter =  painterResource(id = if(isChecked) R.drawable.ic_action_ok else R.drawable.ic_action_no),"",
                modifier = Modifier.offset(x=5.dp,y= (-5).dp))

        }

//        Image(painter = painterResource(R.drawable.document),"")
        Column {
            Text(it.name)
            Text(it.absolutePath,
                color = Color.LightGray,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
    }
}

