package com.example.sy.Leave

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.lib_base.BaseTitle
import com.example.lib_base.Const
import com.example.lib_base.Entity.LeaveEntity
import com.example.lib_base.Entity.LeaveEntityItem
import com.example.sy.R
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

@Composable
fun LeaveXq(navHostController: NavHostController,json :String) {

    val leave = Gson().fromJson(json, LeaveEntityItem::class.java)


    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        BaseTitle(title = "请假详情", navHostController = navHostController)
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

        Text("请假详情",color=Color.Blue)
        Spacer(modifier = Modifier.height(20.dp))

        Text("请假方式:${leave.type}")
        Text("开始时间:${leave.startTime}")
        Text("结束时间:${leave.deptName}")
        Text("请假时长:${leave.hours}")
        Text("请假原因:${leave.reason}")
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                var img = if (leave.status=="通过") R.drawable.ic_action_ok else R.drawable.ic_action_refuse
                Image(painterResource(img),"",
                    modifier = Modifier.size(40.dp).clip(CircleShape))
                Divider(modifier = Modifier.width(1.dp).height(80.dp))
                Image(painterResource(img),"",
                    modifier = Modifier.size(30.dp).clip(CircleShape))
            }


            Column (modifier = Modifier.height(150.dp), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween){
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("已提交")
                    Text("申请时间:${leave.startTime}", fontSize = 10.sp, color = Color.Gray)
                }
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(leave.status)
                    Text("审批时间:${leave.comTime}", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }

    }
}