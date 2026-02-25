package com.example.sy.Page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.document.Page.DocumentPage
import com.example.lib_base.Const
import com.example.sy.Apps.AppsPage
import com.example.sy.Clock.AddsSignPage
import com.example.sy.Clock.ClockPage
import com.example.sy.Leave.AddLeavePage
import com.example.sy.Leave.ComLeavePage
import com.example.sy.Leave.ComLeaveXqPage
import com.example.sy.Leave.LeavePage
import com.example.sy.Leave.LeaveXq
import com.example.sy.R
import com.example.sy.Video.VideoPage
import com.example.sy.Video.VideoPlay
import com.example.sy.WorkLog.AddWorkLogPage
import com.example.sy.WorkLog.ScWorkLogPage
import com.example.sy.WorkLog.WorkLogPage
import com.example.user.Page.LoginPage
import com.example.user.Page.RegisterPage


/**
 * 导航项密封类，用于定义导航菜单中的各个页面
 * @property path 页面路径，用于导航到特定页面
 * @property img 页面图标资源ID
 * @property title 页面标题
 */



sealed class NavItem(val path:String,val img:Int,val title:String){
    // 第一个页面的导航项
    data object Page1:NavItem(Const.One, R.drawable.img,"界面1")
    // 第二个页面的导航项
    data object Page2:NavItem(Const.Two, R.drawable.img,"界面2")
    // 第三个页面的导航项
    data object Page3:NavItem(Const.Three, R.drawable.img,"我的")
}

@Composable
fun MainPage(modifier: Modifier = Modifier) {
// 创建一个包含三个导航项的列表
    val list = listOf(NavItem.Page1,NavItem.Page2,NavItem.Page3)
// 创建一个导航控制器，用于管理导航状态
    val navHostController = rememberNavController()

//    TwoPage(navHostController = navHostController)

// 使用 rememberSaveable 和 mutableStateOf 创建一个可保存的状态变量
// 用于跟踪当前选中的路由页面，初始值为 First 页面
    var selectRoute by rememberSaveable {
        mutableStateOf(Const.Two)

    }

    // 使用navHostController的currentBackStackEntryAsState获取当前导航堆栈条目的状态
    val stack by navHostController.currentBackStackEntryAsState()
    // 从堆栈中获取当前导航目标
    val destination = stack?.destination

    Scaffold (bottomBar = {
        // 检查目标目的地的层级结构中是否包含导航列表中的任何路径
        if (
            // 使用安全调用操作符(?.)访问destination对象的hierarchy属性
            destination?.hierarchy?.any{
                // 将导航列表(list)中的每个导航项(navItem)映射为其路径(path)
                list.map {navItem->
                    navItem.path
                // 检查当前层级中的路由是否存在于映射后的路径列表中
                }.contains(it.route)
            } == true){
            NavigationBar {
                list.forEach {
                    NavigationBarItem(selected = selectRoute == it.path,
// 点击事件处理：当导航项被点击时，执行导航操作并更新当前选中的路由
                        onClick = {
    // 使用 navHostController 导航到指定路径
                            navHostController.navigate(it.path)
    // 更新当前选中的路由路径
                            selectRoute = it.path
                        }, icon = {
                            Image(painter = painterResource(it.img),"", modifier = Modifier.size(30.dp),
                                colorFilter = ColorFilter.tint(if(selectRoute == it.path) Color.Red else Color.Gray))
                        }, label = {
                            Text(it.title)
                        }, colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = Color.Red,
                            unselectedIconColor = Color.Gray
                        ))
                }
            }
        }
    }){
        // 使用NavHost作为导航容器，并应用内边距修饰符
        // it参数通常是从lambda表达式或函数参数中传递进来的PaddingValues
        NavHost(modifier = Modifier.padding(it),
            navController = navHostController ,
            startDestination = Const.Two){
            composable(Const.Two){ OnePage(navHostController = navHostController) }
            composable(Const.One){ TwoPage(navHostController = navHostController) }
            composable(Const.Three){ ThreePage(navHostController = navHostController) }
            composable(Const.loginPage){ LoginPage(navHostController = navHostController) }
            composable(Const.registerPage){ RegisterPage(navHostController = navHostController) }


            composable(Const.AppsPage){ AppsPage(navHostController = navHostController) }

            composable(Const.DocumentPage){ DocumentPage(navHostController = navHostController) }
            composable(Const.WorkLogPage){ WorkLogPage(navHostController = navHostController) }
            composable(Const.AddWorkLogPage+"/{json}"){
                val s = it.arguments?.getString("json").toString()
                AddWorkLogPage( navHostController = navHostController, json = s)
            }
            composable(Const.ScWorkLogPage){ ScWorkLogPage() }
            composable(Const.ClockPage){ ClockPage(navHostController = navHostController) }
            composable(Const.AddSignPage){ AddsSignPage(navHostController = navHostController) }
            composable(Const.LeavePage){ LeavePage(navHostController = navHostController) }
            composable(Const.AddLeave){ AddLeavePage(navHostController = navHostController) }
            composable(Const.LeaveXq+"/{json}"){
                val s = it.arguments?.getString("json").toString()
                LeaveXq(navHostController = navHostController,json = s) }
            composable(Const.ComLeave){ ComLeavePage(navHostController = navHostController) }
            composable(Const.ComLeaveXq+"/{json}"){
                val s = it.arguments?.getString("json").toString()
                ComLeaveXqPage(navHostController = navHostController,json = s) }
            composable(Const.VideoPage){ VideoPage(navHostController = navHostController) }

            composable(Const.VideoPlay){ VideoPlay(navHostController = navHostController) }

        }
    }

}