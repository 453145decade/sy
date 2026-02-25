package com.example.sy.Leave

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.BaseTitle
import com.example.lib_base.UiStare
import com.example.sy.Leave.LeaveRepo
import com.example.sy.Leave.LeaveViewModel
import com.example.sy.R
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.chrono.Chronology
import java.time.temporal.ChronoUnit

lateinit var start : LocalDate
lateinit var end : LocalDate
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLeavePage(navHostController: NavHostController,vm:LeaveViewModel = hiltViewModel()) {

    var type by remember {
        mutableStateOf("请假")
    }
    var startTime by remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()))
    }
    var endTime by remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()))
    }
    var isStartTimeShow by remember {
        mutableStateOf(false)
    }
    var isEndTimeShow by remember {
        mutableStateOf(false)
    }
    var hour by remember {
        mutableStateOf("")
    }
    var reason by remember {
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
                    }
                }
            }
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(10.dp)) {
        BaseTitle("添加请假", navHostController = navHostController)
        Card(onClick = {  }, modifier = Modifier
            .padding(10.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp)) {
                AsyncImage(model = "https://bkimg.cdn.bcebos.com/pic/a2cc7cd98d1001e939011eb89b546cec54e736d13948?x-bce-process=image/format,f_auto/watermark,image_d2F0ZXIvYmFpa2UyNzI,g_7,xp_5,yp_5,P_20/resize,m_lfit,limit_1,h_1080", contentDescription = "", modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape))
                Column {
                    MMKV.defaultMMKV().decodeString("username","未登录")?.let { Text(text = it) }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "员工")
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "请假类型")
            RadioButton(selected = type == "请假", onClick = { type = "请假" })
            Text(text = "请假")
            RadioButton(selected = type == "调休", onClick = { type = "调休" })
            Text(text = "调休")
            RadioButton(selected = type == "加班", onClick = { type = "加班" })
            Text(text = "加班")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "开始时间")
            OutlinedTextField(value = startTime, onValueChange = {startTime = it}, readOnly = true,modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = "", modifier = Modifier.clickable {
                    isStartTimeShow = true
                })
                })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "结束时间")
            OutlinedTextField(value = endTime, onValueChange = {endTime = it}, readOnly = true,modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = "", modifier = Modifier.clickable {
                    isEndTimeShow = true
                })})
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "请假时长")
            OutlinedTextField(value = hour, onValueChange = {hour = it}, readOnly = true,modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Text(text = "小时")})
        }
        var isError by remember {
            mutableStateOf(false)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "请假原因")
            OutlinedTextField(value = reason, onValueChange = {
                if (it.length>20){
                    isError = true
                    ToastUtils.showLong("字数超过限制")
                }else{
                    isError = false
                    reason = it
                }
            },modifier = Modifier.fillMaxWidth(),isError = isError,
                trailingIcon = { Text(text = "${reason.length}/20")})
        }
//        按钮
        Button(onClick = {
            var map = mapOf(
                "type" to type,
                "startTime" to startTime,
                "endTime" to endTime,
                "hours" to hour,
                "reason" to reason
            )
            vm.sendIntent(LeaveIntent.addLeave(map))
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "提交")
        }
    }

    val state = rememberDatePickerState()
    if (isStartTimeShow||isEndTimeShow){
        DatePickerDialog(onDismissRequest = {
            isStartTimeShow = false
            isEndTimeShow = false
        }, confirmButton = {
            TextButton(onClick = {
            val time = SimpleDateFormat("yyyy-MM-dd").format(state.selectedDateMillis)
            if (isStartTimeShow){
                startTime = "$time 09:00:00"
                isStartTimeShow = false
                start=LocalDate.parse(time)
            }else{
                endTime = "$time 18:00:00"
                isEndTimeShow = false
                end=LocalDate.parse(time)
                hour = (ChronoUnit.DAYS.between(start,end)*24+24).toString()
            }
        }) {
            Text(text = "OK")
        } }) {
            DatePicker(state = state)
        }
    }
}