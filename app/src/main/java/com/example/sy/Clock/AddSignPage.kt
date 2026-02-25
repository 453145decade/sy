package com.example.sy.Clock

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.example.lib_base.BaseApp
import com.example.lib_base.Const
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Timer
import kotlin.concurrent.timerTask

lateinit var timer: Timer
lateinit var mapView : MapView
lateinit var map:AMap
lateinit var myLocationStyle: MyLocationStyle
lateinit var geocodeSearch: GeocodeSearch
lateinit var regecodeQuery: RegeocodeQuery
@Composable
fun AddsSignPage(navHostController: NavHostController,vm : ClockViewModel = hiltViewModel()) {

//    记录上班打卡时间
    var startTime by remember {
        mutableStateOf(MMKV.defaultMMKV().decodeString(Const.StartTime,"上班还未打卡").toString())
    }
//    记录下班打卡时间
    var endTime by remember {
        mutableStateOf(MMKV.defaultMMKV().decodeString(Const.EndTiem,"下班还未打卡").toString())
    }
//    打卡类型
    var btnName by remember {
        mutableStateOf("上班打卡")
    }
//    当前时间
    var nowTime by remember {
        mutableStateOf("")
    }
//    上班状态
    var status by remember {
        mutableStateOf("正常")
    }
//    按钮是否可点击
    var isEnable by remember {
        mutableStateOf(true)
    }

    var lat by remember {
        mutableStateOf(0.0)
    }
    var lon by remember {
        mutableStateOf(0.0)
    }
    var type by remember {
        mutableStateOf("")
    }
    var address by remember {
        mutableStateOf("河北省邯郸市邯郸东站")
    }

    LaunchedEffect("") {
        launch {
            timer = Timer()
            timer.schedule(timerTask {
                nowTime = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())
            },0,1000)
        }
        launch {
            // 从MMKV中解码存储的签到时间字符串
            val sigIn = MMKV.defaultMMKV().decodeString(Const.StartTime)
            // 检查签到时间字符串是否为空
            if(!TextUtils.isEmpty(sigIn)){
                // 获取当前时间的格式化字符串（年-月-日 时:分:秒）
                val data =
                    SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())
                // 从签到时间中提取日期部分（前10个字符）
                val startDate = sigIn?.substring(0, 10)
                // 检查签到日期是否等于当前日期
                if (startDate == data){
                    // 从MMKV中解码存储的上班状态
                    val statusIn = MMKV.defaultMMKV().decodeString(Const.Status_in)
                    // 设置上班打卡信息，包含签到时间和状态
                    startTime="上班时间："+sigIn+"  "+statusIn
                    // 设置按钮名称为"下班打卡"
                    btnName = "下班打卡"
                    // 从MMKV中解码存储的签退时间字符串
                    val signOut = MMKV.defaultMMKV().decodeString(Const.EndTiem)
                    // 检查签退时间字符串是否为空
                    if (!TextUtils.isEmpty(signOut)){
                        // 从签退时间中提取日期部分（前10个字符）
                        val endDate = signOut?.substring(0, 10)
                        // 检查签退日期是否等于当前日期
                        if (endDate == data){
                            // 从MMKV中解码存储的下班状态
                            val statusOut = MMKV.defaultMMKV().decodeString(Const.Status_out)
                            // 设置下班打卡信息，包含签退时间和状态
                            endTime = "下班时间："+signOut +"  "+statusOut
                            // 禁用打卡按钮
                            isEnable = false
                        }
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {


        Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // 使用AndroidView组件来嵌入原生Android地图视图
            AndroidView(factory = {
                // 创建MapView实例并设置其上下文
                mapView= MapView(it)
                // 调用MapView的onCreate方法，传入null表示使用默认配置
                mapView.onCreate(null)
                // 获取地图实例
                map=mapView.map
                // 创建定位样式对象
                myLocationStyle = MyLocationStyle()
                // 设置定位更新的时间间隔为2000毫秒
                myLocationStyle.interval(5000)
                // 将定位样式应用到地图上
                map.myLocationStyle = myLocationStyle
                // 启用地图的定位功能
                map.isMyLocationEnabled = true

                // 创建地理编码搜索对象
                geocodeSearch = GeocodeSearch(it)

                // 添加定位变化监听器
                map.addOnMyLocationChangeListener {loca->


                    // 创建逆地理编码查询对象，使用当前位置和2000米的范围
                    regecodeQuery = RegeocodeQuery(
                        LatLonPoint(loca.latitude, loca.longitude),
                        2000f,
                        GeocodeSearch.AMAP
                    )

                    // 异步执行逆地理编码查询
                    geocodeSearch.getFromLocationAsyn(regecodeQuery)

                    // 设置地理编码搜索结果监听器
                    geocodeSearch.setOnGeocodeSearchListener(object : OnGeocodeSearchListener{
                        // 当逆地理编码搜索完成时调用
                        override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
                            // 获取格式化后的地址信息
                            val address = p0?.regeocodeAddress?.formatAddress
                            // 打印定位地址和错误码
                            Log.e("TAG", "我的定位位置:$address,错误:$p1 " )
                            // 保存经纬度信息
                            lat = loca.latitude
                            lon = loca.longitude
                        }

                        // 当地理编码搜索完成时调用（本例中未实现具体功能）
                        override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {

                            // 此处暂未实现具体功能
                        }

                    })
                }


                // 返回MapView实例作为AndroidView的内容
                mapView
            }, modifier = Modifier.height(300.dp))

            Spacer(modifier = Modifier.size(10.dp))


            Text(text = ""+startTime, modifier = Modifier.fillMaxWidth())
            Text(text = ""+endTime, modifier = Modifier.fillMaxWidth())

            Button(onClick = {
                val time =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())
                if (btnName == "上班打卡") {

                    if (time.substring(11,19)>"09:00:00"){
                        status = "迟到"
                    }else{
                        status = "正常"
                    }
                    MMKV.defaultMMKV().encode(Const.Status_in,status)
                    MMKV.defaultMMKV().encode(Const.StartTime,time)
                    startTime = "上班时间："+time +"  "+status
                    btnName = "下班打卡"
                    type = "上班"
                }else{
                    if (time.substring(11,19)<"17:00:00"){
                        status = "早退"
                    }else{
                        status = "正常"
                    }
                    MMKV.defaultMMKV().encode(Const.Status_out,status)
                    MMKV.defaultMMKV().encode(Const.EndTiem,time)
                    endTime = "下班时间："+time +"  "+status
                    isEnable = false
                    type = "下班"
                }

                val map = mapOf(
                    "lat" to lat,
                    "lon" to lon,
                    "type" to type,
                    "status" to status,
                    "address" to address
                )
                vm.sendIntent(ClockIntent.addClock(map))

            }, modifier = Modifier.size(150.dp).clip(CircleShape).align(Alignment.CenterHorizontally), enabled = isEnable) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Text(""+btnName)
                    Text(""+nowTime)
                }
            }

        }


    }
}