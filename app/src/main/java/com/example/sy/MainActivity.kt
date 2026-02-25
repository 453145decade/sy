package com.example.sy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.document.Page.DocumentPage
import com.example.lib_base.Const
import com.example.sy.Apps.AppsPage
import com.example.sy.Clock.AddsSignPage
import com.example.sy.Clock.ClockPage
import com.example.sy.Goods.CarPage
import com.example.sy.Goods.GoodsPage
import com.example.sy.Goods.GoodsXQ
import com.example.sy.Goods.PayPage
import com.example.sy.Leave.AddLeavePage
import com.example.sy.Leave.LeavePage
import com.example.sy.Leave.LeaveXq
import com.example.sy.Page.MainPage
import com.example.sy.Page.OnePage
import com.example.sy.Page.ThreePage
import com.example.sy.Page.TwoPage
import com.example.sy.Video.VideoPage
import com.example.sy.Video.VideoPlay
import com.example.sy.View.ViewPaint
import com.example.sy.WorkLog.AddWorkLogPage
import com.example.sy.WorkLog.ScWorkLogPage
import com.example.sy.WorkLog.WorkLogPage

import com.example.sy.ui.theme.SyTheme
import com.example.user.Page.LoginPage
import com.example.user.Page.RegisterPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MainPage()






                    requestPermissions(arrayOf(
                        "android.permission.VIBRATE",
                        "android.permission.RECORD_AUDIO" ,
                        "android.permission.CAMERA",
                        "android.permission.ACCESS_NETWORK_STATE",
                        "android.permission.WRITE_EXTERNAL_STORAGE" ,
                        "android.permission.ACCESS_FINE_LOCATION",
                        "android.permission.GET_TASKS" ,
                        "android.permission.ACCESS_WIFI_STATE" ,
                        "android.permission.CHANGE_WIFI_STATE" ,
                        "android.permission.WAKE_LOCK",
                        "android.permission.MODIFY_AUDIO_SETTINGS",
                        "android.permission.READ_PHONE_STATE",
                        "android.permission.RECEIVE_BOOT_COMPLETED",
                        "android.permission.FOREGROUND_SERVICE",
                        "android.permission.ACCESS_COARSE_LOCATION"
                    ),101)

                    val navHostController = rememberNavController()


//                    ViewPaint(navHostController = navHostController)



                    NavHost(
                        navController = navHostController ,
                        startDestination = Const.MainPage){
                        composable(Const.WebPage){ WebPage() }
                        composable(Const.MainPage){ MainPage() }
                        composable(Const.One){ OnePage(navHostController = navHostController) }
                        composable(Const.Two){ TwoPage(navHostController = navHostController) }
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
                        composable(Const.LeaveXq+"/{json}"){
                            val s = it.arguments?.getString("json").toString()
                            LeaveXq(navHostController = navHostController,json = s) }
                        composable(Const.AddLeave){ AddLeavePage(navHostController = navHostController) }
                        composable(Const.VideoPage){ VideoPage(navHostController = navHostController) }
                        composable(Const.VideoPlay){ VideoPlay(navHostController = navHostController) }

                        composable(Const.GoodsPage){ GoodsPage(navHostController = navHostController) }
                        composable(Const.GoodsXQ){ GoodsXQ(navHostController = navHostController) }
                        composable(Const.CarPage){ CarPage(navHostController = navHostController) }
                        composable(Const.PayPage){ PayPage(navHostController = navHostController) }
                    }

                }
            }
        }
    }
}
