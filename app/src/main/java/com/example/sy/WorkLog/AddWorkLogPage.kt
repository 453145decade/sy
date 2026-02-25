package com.example.sy.WorkLog

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.BaseTitle
import com.example.lib_base.Const
import com.example.lib_base.UiStare
import com.example.sy.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Composable
fun AddWorkLogPage(modifier: Modifier = Modifier, navHostController: NavHostController,json:String,vm:WorkLogViewModel = hiltViewModel()) {
//    Text(text = json, modifier = modifier)

    var title by remember {
        mutableStateOf("")
    }
    var content by remember {
        mutableStateOf("")
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
                        ToastUtils.showLong("添加成功")
                        navHostController.popBackStack()
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()
        .background(color = colorResource(R.color.h))) {
        BaseTitle(title = "日志编辑", navHostController = navHostController)
        val date= SimpleDateFormat("yyyy-MM-dd").format(/* obj = */ System.currentTimeMillis())
        val week= TimeUtils.getChineseWeek(System.currentTimeMillis())//星期

        Column (modifier = Modifier.fillMaxWidth().padding(10.dp).background(androidx.compose.ui.graphics.Color.White)){
            Text(text = "$date $week 今天", modifier = Modifier.padding(10.dp))
        }
        Column (modifier = Modifier.fillMaxWidth().padding(10.dp).background(androidx.compose.ui.graphics.Color.White),
            verticalArrangement = Arrangement.Center){
            Text(text = "今日内容",modifier = Modifier.padding(10.dp))
        }
        TextField(value = title, onValueChange = {title=it}, label = {
            Text(text = "请输入${json}的标题", maxLines = 1,modifier = Modifier.padding(10.dp))
        }, modifier = Modifier.fillMaxWidth().padding(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = androidx.compose.ui.graphics.Color.White,
                unfocusedContainerColor = androidx.compose.ui.graphics.Color.White
            ))

        TextField(value = content, onValueChange = {content=it}, label = {
            Text(text = "请输入${json}的内容",modifier = Modifier.padding(10.dp))
        }, modifier = Modifier
            .fillMaxWidth().height(300.dp).padding(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = androidx.compose.ui.graphics.Color.White,
                unfocusedContainerColor = androidx.compose.ui.graphics.Color.White
            ))
        Button(onClick = {
            if (title.isNotEmpty()&&content.isNotEmpty()){
                val map = mapOf(
                    Const.addWorkLogTitle to title,
                    Const.addWorkContext to content
                )
                vm.sendIntent(WorkLogIntent.addWorkLog(map))
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "提交")
        }

    }
}