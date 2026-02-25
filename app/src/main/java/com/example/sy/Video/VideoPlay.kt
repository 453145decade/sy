package com.example.sy.Video

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.BaseApp
import com.example.lib_base.Entity.VideoComment
import com.example.lib_base.Entity.VideoCommentItem
import com.example.lib_base.Entity.VideoEntityItem
import com.example.lib_base.StateType
import com.example.lib_base.StateType.*
import com.example.lib_base.UiStare
import com.example.sy.Clock.timer
import com.example.sy.Leave.end
import com.example.sy.R
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.umeng.socialize.ShareAction
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMVideo
import com.umeng.socialize.media.UMWeb
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Timer
import kotlin.concurrent.timerTask


lateinit var timerVideo: Timer
@Composable
fun VideoPlay(navHostController: NavHostController,vm:VideoViewModel= hiltViewModel()) {

//    val string = SPUtils.getInstance().getString("videourl")


    var list = remember {
        mutableStateListOf<VideoCommentItem>()
    }

    var isShowComment by remember {
        mutableStateOf(false)
    }
    val json = MMKV.defaultMMKV().decodeString("video")
    val video = Gson().fromJson(json, VideoEntityItem::class.java)

    val context = LocalContext.current
    var videoDZ by remember {
        mutableStateOf(SPUtils.getInstance().getString("videoDZ"+video.id))
    }
    var dzc by remember {
        mutableStateOf(video.like_count)
    }
    var videoSC by remember {
        mutableStateOf(SPUtils.getInstance().getString("videoSC"+video.id))
    }
    var commented by remember {
        mutableStateOf("")
    }
    var commentc by remember {
        mutableStateOf(0)
    }
    var isFocus by remember {
        mutableStateOf(false)
    }
    var time by remember {
        mutableStateOf(0)
    }

    // 使用 LaunchedEffect 在协程作用域中执行代码块
    // 当 LaunchedEffect 进入组合时，会启动一个协程来执行其代码块
    // 参数 Unit 表示当 LaunchedEffect 进入组合时立即执行
    LaunchedEffect(Unit) {
        launch {
            while (true){
                time++
                delay(1000)
            }
        }
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
                        when(it.stateType){
                            DEFAULT -> {

                            }
                            DOWNLOAD -> {
                                ToastUtils.showLong(it.data.toString())
                                isFocus=true
                            }
                            Scan -> {
                                ToastUtils.showLong(it.data.toString())
                                isFocus=false
                            }
                            INSERT -> {

                            }
                            del -> {

                            }

                            comment -> {
                                list.clear()
                                list.addAll((it.data as VideoComment).sortedByDescending { it.createtime })
                            }
                            send -> {
                                ToastUtils.showLong("提交评论成功")
                                vm.sendIntent(VideoIntent.videoComment(video.id))
                            }
                        }
                    }
                }
            }
        }
        launch {
            if (video.guanzhu==1){
                isFocus = true
            }
        }
    }



    val exoPlayer = remember {
// 使用ExoPlayer.Builder创建并构建ExoPlayer实例
        ExoPlayer.Builder(context).build().apply {
    // 设置媒体资源，从视频路径创建MediaItem
            setMediaItem(MediaItem.fromUri(video.videopath))
    // 准备播放器
            prepare()
    // 设置为自动开始播放
            playWhenReady = true
    // 设置重复模式为单曲循环
            repeatMode = REPEAT_MODE_ONE
        }
    }


        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd){
            AndroidView(factory = {
// 创建一个PlayerView实例，并应用一些配置
// it: 这里是作为lambda参数传入的View或Context对象
                PlayerView(it).apply {
    // 设置播放器实例
    // exoPlayer: 已经初始化的ExoPlayer播放器实例
                    player = exoPlayer
    // 启用/禁用默认播放控制器
    // true: 显示播放控制器，包含播放/暂停、进度条、音量控制等
                    useController = true
                }
            }, modifier = Modifier.fillMaxSize())

            Text("直播时长"+SimpleDateFormat("HH:mm:ss").format(time*1000 - 8*60*60*1000),
                color = Color.White, modifier = Modifier.padding(bottom = 580.dp, end = 20.dp))
            Column(modifier = Modifier.padding(end = 10.dp, bottom = 100.dp)) {
                Box(contentAlignment = Alignment.BottomCenter){
                    AsyncImage(model = video.headpath,"", modifier = Modifier
                        .size(40.dp)
                        .clip(
                            CircleShape
                        )
                        .clickable {
                            val map = mapOf(
                                "authname" to video.authname
                            )
                            vm.sendIntent(VideoIntent.videoQG(map))
                        })
                    if (!isFocus){
                        Icon(imageVector = Icons.Default.Add,"", tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
// 设置垂直偏移量为10个dp单位
                                .offset(y = 10.dp)
// 设置背景颜色为红色，并使用圆形形状
                                .background(
                                    Color.Red,    // 背景颜色设置为红色
                                    CircleShape   // 背景形状设置为圆形
                                )
                                .clickable {
                                    val map = mapOf(
                                        "authname" to video.authname
                                    )
                                    vm.sendIntent(VideoIntent.videoGZ(map))
                                })
                    }

                }
                Spacer(modifier = Modifier.size(20.dp))

                val i =
                    if (videoDZ == "1") R.drawable.baseline_favorite_241 else R.drawable.baseline_favorite_24
                Image(painter = painterResource(i),"", modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        if (videoDZ == "1") {
                            SPUtils
                                .getInstance()
                                .put("videoDZ" + video.id, "0")
                            videoDZ = "0"
                            dzc = video.like_count
                            ToastUtils.showLong("取消点赞成功")
                        } else {
                            SPUtils
                                .getInstance()
                                .put("videoDZ" + video.id, "1")
                            videoDZ = "1"
                            dzc = video.like_count + 1
                            ToastUtils.showLong("点赞成功")
                        }

                    })
                dzc = if (videoDZ == "1") video.like_count + 1 else video.like_count
                Text(dzc.toString(), color = Color.White)
                Spacer(modifier = Modifier.size(10.dp))

                Image(painter = painterResource(R.drawable.baseline_message_24),"", modifier = Modifier.size(40.dp).clickable {
                    isShowComment = true
                    vm.sendIntent(VideoIntent.videoComment(video.id))
                })
                Text("评论", color = Color.White)
                Spacer(modifier = Modifier.size(10.dp))

                val i1 =
                    if (videoSC == "1") R.drawable.baseline_star_241 else R.drawable.baseline_star_24
                Image(painter = painterResource(i1),"", modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        if (videoSC == "1") {
                            SPUtils
                                .getInstance()
                                .put("videoSC" + video.id, "0")
                            videoSC = "0"
                            ToastUtils.showLong("取消收藏成功")
                        } else {
                            SPUtils
                                .getInstance()
                                .put("videoSC" + video.id, "1")
                            videoSC = "1"
                            ToastUtils.showLong("收藏成功")
                        }
                    })


                Text("收藏", color = Color.White)
                Spacer(modifier = Modifier.size(10.dp))

                Image(painter = painterResource(R.drawable.baseline_share_24),"", modifier = Modifier
                    .size(40.dp)
                    .clickable {

                        val image: UMImage = UMImage(BaseApp.context, "imageurl") //网络图片
                        val video1 = UMWeb(video.videopath)
                        video1.title = ""+video.caption //视频的标题
                        video1.setThumb(image) //视频的缩略图
                        video1.description = ""+video.caption //视频的描述

                        ShareAction(context as Activity)
                            .withText("hello")
                            .withMedia(video1)
                        .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                        .open();

                    })
                Text("分享", color = Color.White)
                Spacer(modifier = Modifier.size(10.dp))
            }

            if (isShowComment){
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        color = Color.White,
                        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("评论数量"+list.size, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                        Icon(Icons.Default.Clear,"", modifier = Modifier.size(40.dp).clickable {
                            isShowComment = false
                        }.padding(end = 15.dp))
                    }
                    LazyColumn(modifier = Modifier.fillMaxWidth().padding(10.dp).weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(list){
                            videoCommentItem(it)
                        }
                    }
                    OutlinedTextField(value = commented, onValueChange = {commented = it},
                        label = {
                            Text("请输入评论内容")
                        },
                        modifier = Modifier.fillMaxWidth(), trailingIcon = {
                        Icon(Icons.Default.Send,"", modifier = Modifier.size(40.dp).clickable {
                            val map = mapOf(
                                "msg" to commented,
                                "videoId" to video.id
                            )
                            vm.sendIntent(VideoIntent.sendVideoComment(map))
                            commented = ""
                        })
                    })
                }
            }

        }

//    AndroidView(factory = {
//        PlayerView(it).apply {
//            player = exoPlayer
//            useController = true
//        }
//    }, modifier = Modifier.fillMaxSize())

// 使用DisposableEffect来管理资源释放
// 当组件被销毁时，会自动执行onDispose块中的代码
    DisposableEffect(Unit) {
    // 在组件销毁时释放ExoPlayer资源，防止内存泄漏
        onDispose {
            exoPlayer.release()
        }
    }

}

@Composable
fun videoCommentItem(it: VideoCommentItem) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            AsyncImage(model = it.icon,"", modifier = Modifier.size(50.dp).clip(CircleShape))
            Spacer(modifier = Modifier.size(10.dp))
            Column {
                Text("评论人  ："+it.username)
                Text("评论内容  ："+it.msg)
                Text("评论时间  ："+it.createtime)
            }
        }
        Spacer(modifier = Modifier.size(10.dp))

    }

}
