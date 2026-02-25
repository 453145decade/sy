package com.example.sy.Page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.lib_base.Const
import com.example.sy.R
import com.tencent.mmkv.MMKV

@Composable
fun ThreePage(navHostController: NavHostController) {
    var name by remember {
        mutableStateOf(MMKV.defaultMMKV().decodeString(Const.logName,"未登录").toString())
    }
    var role by remember {
        mutableStateOf(MMKV.defaultMMKV().decodeString(Const.logRole,"员工").toString())
    }
    var isshowloginOut by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
        Text(text = "我的", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Card (border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier.fillMaxWidth().padding(10.dp).clickable {
                if (name=="未登录"){
                    navHostController.navigate(Const.loginPage)
                }else{
                    isshowloginOut=true
                }
            },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )){
            Row (modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)){
                AsyncImage(model = "https://bkimg.cdn.bcebos.com/pic/a2cc7cd98d1001e939011eb89b546cec54e736d13948?x-bce-process=image/format,f_auto/watermark,image_d2F0ZXIvYmFpa2UyNzI,g_7,xp_5,yp_5,P_20/resize,m_lfit,limit_1,h_1080", contentDescription = "",
                    modifier = Modifier.size(60.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop)
                Column (modifier = Modifier.height(60.dp), verticalArrangement = Arrangement.SpaceAround){
                    Text(text = name, color = Color.Black)
                    Text(text = role, color = Color.Black)
                }
            }
        }
        Card (border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)){
            Row (modifier = Modifier.padding(10.dp),horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically){
                Column (modifier = Modifier.clickable {
                    navHostController.navigate(Const.LeavePage)
                }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)){
                    Image(painter = painterResource(id= R.drawable.report), contentDescription = "",modifier = Modifier.size(40.dp))
                    Text(text = "请假")
                }
                Column (modifier = Modifier.clickable {
                    navHostController.navigate(Const.ComLeave)
                }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)){
                    Image(painter = painterResource(id= R.drawable.question), contentDescription = "",modifier = Modifier.size(40.dp))
                    Text(text = "审批")
                }
                Column (modifier = Modifier.clickable {
//                    navHostController.navigate(RouthPath.VIDEOSTORE)
                }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)){
                    Image(painter = painterResource(id= R.drawable.question), contentDescription = "",modifier = Modifier.size(40.dp).clickable {
                        navHostController.navigate(Const.ScWorkLogPage)
                    })
                    Text(text = "收藏")
                }
            }
        }
    }
    // 判断是否显示退出登录的对话框
    if(isshowloginOut){
        AlertDialog(onDismissRequest = {isshowloginOut=false}, confirmButton = {
            Text(text = "确认", modifier = Modifier.clickable {
                MMKV.defaultMMKV().remove(Const.Token)
                MMKV.defaultMMKV().remove(Const.logName)
                MMKV.defaultMMKV().remove(Const.logRole)
                name="未登录"
                role="员工"
                isshowloginOut=false
            })
        }, title = {
            Text(text = "推出提醒")
        }, text = {
            Text("是否退出当前账号")
        })
    }
    /* var name by remember {
         mutableStateOf( MMKV.defaultMMKV().decodeString(Consl.PARAM_USERNAME,"未登录"))
     }
     var list= remember {
         SnapshotStateList<StoreEntity>()
     }
     LaunchedEffect(key1 = "") {
         launch {
             vm.state.collect{
                 when(it){
                     is UIState.OnError -> {
                         ToastUtils.showLong("数据库获取失败${it.msg}")
                     }
                     UIState.OnLoading -> {}
                     is UIState.OnSuccess<*> -> {
                         if(it.stateType==StateType.DEFAULT) {
                             list.clear()
                             list.addAll(it.data as List<StoreEntity>)
                         }
                         if (it.stateType==StateType.DEL){
                             ToastUtils.showLong("删除成功")
                         }
                     }
                 }
             }
         }
         vm.sendIntent(MineIntent.queryall)
     }
     Column {
    /*     Text(text = "我的", modifier = Modifier.clickable {
             //navHostController.navigate(RouthPath.REGISTER)
             val user= User("mz666","032958")
             val json= Gson().toJson(user)
             navHostController.navigate(RouthPath.LOGIN+"/$json")
         })
         Text(text = name.toString())*/
         LazyColumn (modifier = Modifier.fillMaxWidth(),
             verticalArrangement = Arrangement.spacedBy(10.dp)){
             items(list){
                 ReportItemmm(it){
                     vm.sendIntent(MineIntent.delstore(it))
                     list.remove(it)
                 }
             }
         }
     }*/
}