package com.example.sy.Leave

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.BaseTitle
import com.example.lib_base.Entity.LeaveEntityItem
import com.example.lib_base.StateType
import com.example.lib_base.StateType.*
import com.example.lib_base.UiStare
import com.example.sy.R
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Composable
fun ComLeaveXqPage(navHostController: NavHostController,vm:LeaveViewModel = hiltViewModel(),json :String) {

    var role by remember {
        mutableStateOf("")
    }
    var te by remember {
        mutableStateOf("")
    }

    val leave = Gson().fromJson(json, LeaveEntityItem::class.java)

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
                                ToastUtils.showLong("提交成功")
                                navHostController.popBackStack()
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
    }

    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        BaseTitle(title = "请假详情", navHostController = navHostController)

        Text("申报人信息", color = colorResource(R.color.purple_700))
        Card (border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )){
            Row (modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)){
                AsyncImage(model = "https://bkimg.cdn.bcebos.com/pic/a2cc7cd98d1001e939011eb89b546cec54e736d13948?x-bce-process=image/format,f_auto/watermark,image_d2F0ZXIvYmFpa2UyNzI,g_7,xp_5,yp_5,P_20/resize,m_lfit,limit_1,h_1080", contentDescription = "",
                    modifier = Modifier.size(60.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop)
                Column (modifier = Modifier.height(60.dp), verticalArrangement = Arrangement.SpaceAround){
                    Text(text = ""+leave.staffName, color = Color.Black)
                    Text(text = ""+leave.deptName, color = Color.Black)
                }
            }
        }
        Spacer(modifier = Modifier.size(20.dp))
        Text("请假详情", color = colorResource(R.color.purple_700))
        Spacer(modifier = Modifier.size(20.dp))


        Text("请假方式 : "+leave.type)
        Text("开始时间 : "+leave.startTime)
        Text("结束方式 : "+leave.endTime)
        Text("请假时长 : "+leave.hours)
        Text("请假原因 : "+leave.reason)
        Spacer(modifier = Modifier.size(20.dp))
        Text("审批意见")
        Divider(thickness = 1.dp)


        Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            Checkbox(checked = role=="通过", onCheckedChange = {
                if (it){
                    role="通过"
                }
            })
            Text(text = "通过")
            Checkbox(checked = role=="拒绝", onCheckedChange = {
                if (it){
                    role="拒绝"
                }
            })
            Text(text = "拒绝")
        }

        Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            Text("意见")
            TextField(value = te, onValueChange = {
                te = it
            })
        }

        Button(onClick = {
            val map = mapOf(
                "id" to leave.id,
                "status" to role,
                "comTime" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()),
                "comReason" to te,
            )
            vm.sendIntent(LeaveIntent.comLeave(map))
        }, modifier = Modifier.fillMaxWidth()) { Text("提交") }


    }

}