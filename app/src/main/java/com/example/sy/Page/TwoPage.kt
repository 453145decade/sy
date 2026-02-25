package com.example.sy.Page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.Const
import com.example.sy.R

data class TitleEntity(val img:Int,val title:String)
@Composable
fun TwoPage(modifier: Modifier = Modifier,navHostController: NavHostController) {
    var list= listOf(
        TitleEntity(R.drawable.apps,"在线应用"),
        TitleEntity(R.drawable.sign,"考勤打卡"),
        TitleEntity(R.drawable.document,"文件"),
        TitleEntity(R.drawable.report,"工作日志"),
        TitleEntity(R.drawable.shenpi,"智能审批"),
        TitleEntity(R.drawable.question,"任务"),
    )

    val list1= listOf(
        TitleEntity(R.drawable.contact,"联系人"),
        TitleEntity(R.drawable.group,"讨论组"),
        TitleEntity(R.drawable.department,"组织架构")
    )

    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(5.dp)){
        Row (modifier = Modifier.fillMaxWidth().background(colorResource(R.color.danlan)).padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically){
            Text(text = "12", color = Color.White, modifier = Modifier
                .background(colorResource(R.color.pink), CircleShape)
                .padding(5.dp))
            Text("主门户")
        }
        TitleItem(titleEntity = TitleEntity(R.drawable.icon1382,"应用"))

        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(list){
                ContentItem(titleEntity = it){
                    when(it.title){
                        "在线应用"->{
                            navHostController.navigate(Const.AppsPage)
                        }
                        "考勤打卡"->{
                            navHostController.navigate(Const.ClockPage)
                        }
                        "文件"->{
                            navHostController.navigate(Const.DocumentPage)
                        }
                        "工作日志"->{
                            navHostController.navigate(Const.WorkLogPage)
                        }
                        "任务"->{}
                    }
                }
            }
        }

        TitleItem(titleEntity= TitleEntity(R.drawable.icon,"通信"))
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(list1){
                ContentItem(titleEntity = it){
                    when(it.title){
                        "联系人"->{
                            ToastUtils.showLong("点击了联系人")
                            navHostController.navigate(Const.VideoPage)
                        }
                        "讨论组"->{
                            ToastUtils.showLong("点击了讨论组")
                        }
                    }
                }
            }
        }
        TitleItem(titleEntity = TitleEntity(R.drawable.icon,"公众号"))
    }
}


@Composable
fun TitleItem(titleEntity: TitleEntity) {
    Row (modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically){
        Image(painter = painterResource(id = titleEntity.img), contentDescription = "")
        Text(text = titleEntity.title)
    }
}

@Composable
fun ContentItem(titleEntity: TitleEntity,onClick:()->Unit) {
    Column (modifier = Modifier.padding(10.dp).clickable {
        onClick()
    },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)){
        Image(painter = painterResource(id=titleEntity.img), contentDescription = "", modifier = Modifier.size(50.dp))
        Text(text = titleEntity.title)
    }
}