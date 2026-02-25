package com.example.sy.Video

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.Const
import com.example.lib_base.Entity.SearEntity
import com.example.lib_base.Entity.VideoEntity
import com.example.lib_base.Entity.VideoEntityItem
import com.example.lib_base.StateType
import com.example.lib_base.StateType.*
import com.example.lib_base.UiStare
import com.example.sy.Leave.LeaveIntent
import com.example.sy.R
import com.google.gson.Gson
import com.king.ultraswiperefresh.rememberUltraSwipeRefreshState
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun VideoPage(navHostController: NavHostController,vm:VideoViewModel = hiltViewModel()) {

    var search by remember {
        mutableStateOf("")
    }

    var list = remember() {
        SnapshotStateList<VideoEntityItem>()
    }
    var list1 = remember() {
        SnapshotStateList<SearEntity>()
    }
    var searList = remember(list,search) {
        if (search.isNullOrEmpty()){
            list
        }else{
            val filter = list.filter {
                it.authname.contains(search)
            }
            if (filter.isNotEmpty()){
                filter
            }else{
                ToastUtils.showLong("没有找到相关内容")
            }
            filter

        }
    }

    // 使用rememberPagerState记住页面状态，初始值为list的大小
    var pageState = rememberPagerState {
        list.size
    }
    // 使用remember记住一个可变的ExoPlayer列表，用于管理播放器实例
    var playList = remember {
        mutableListOf<ExoPlayer>()
    }
    // 获取当前上下文，用于Android系统相关操作
    val context = LocalContext.current

    var page by remember {
        mutableStateOf(1)
    }
    var size by remember {
        mutableStateOf(20)
    }

    var stare = rememberUltraSwipeRefreshState()


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
                                list.clear()
                                list.addAll(it.data as VideoEntity)
                                vm.sendIntent(VideoIntent.searQuAll)
                            }
                            DOWNLOAD -> {}
                            Scan -> {
                                list1.clear()
                                list1.addAll(it.data as List<SearEntity>)
                            }
                            INSERT -> {

                            }
                            del -> {

                            }
                            comment -> {}
                            send -> {
                                vm.sendIntent(VideoIntent.searQuAll)
                            }
                        }
                    }
                }
            }
        }
        vm.sendIntent(VideoIntent.video(3,25))
    }

    // 使用 remember 记住 derivedStateOf 计算的结果，避免不必要的重复计算
    // currentPage 是一个派生状态，它会根据 pageState.currentPage 的变化而自动更新
    val currentPage by remember {
        // derivedStateOf 用于创建一个派生状态，只有当依赖的 pageState.currentPage 发生变化时才会重新计算
        derivedStateOf {
            // 返回当前页面的状态值
            pageState.currentPage
        }
    }

    LaunchedEffect(currentPage) {
// 遍历播放列表，对每个播放器进行不同的操作
// forEachIndexed 是一个扩展函数，提供了索引和元素值
        playList.forEachIndexed { index, exoPlayer ->
    // 判断当前遍历的播放器索引是否等于当前页码
            if (index == currentPage){
        // 如果是当前页码对应的播放器，则播放
                exoPlayer.play()
            }else{
        // 如果不是当前页码对应的播放器，则准备播放但不播放
                exoPlayer.prepare()
            }
        }
    }

    // 使用DisposableEffect来监听list.size的变化，当list.size改变时执行相应操作
    DisposableEffect(list.size) {
        // 遍历list列表，为每个视频创建一个播放器并添加到playList中
        repeat(list.size){index->
            // 创建播放器并设置媒体源
            playList.add(createPlayer(context).apply {
                // 从视频路径创建MediaItem并设置为播放器的媒体源
                setMediaItem(MediaItem.fromUri(list[index].videopath))
                // 准备播放器
                prepare()
            })
        }

        // 当组件被销毁或list.size改变时，执行清理操作
        onDispose {
            // 遍历playList中的所有播放器，释放资源
            playList.forEach {
                it.release()
            }
        }
    }

////    布局
//    VerticalPager(state = pageState) {index->
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
//            AndroidView(factory = {
//                PlayerView(it).apply {
//                    player = playList[index]
//                    useController = true
//                }
//            }, modifier = Modifier.fillMaxSize())
//            Column(modifier = Modifier.padding(10.dp)) {
//                Box(contentAlignment = Alignment.BottomCenter){
//                    AsyncImage(model = list[index].headpath,"", modifier = Modifier.size(40.dp).clip(
//                        CircleShape
//                    ))
//                    Icon(imageVector = Icons.Default.Add,"", tint = Color.White,
//                        modifier = Modifier.size(20.dp).offset(y = 10.dp).background(Color.Red,
//                            CircleShape))
//
//                }
//                Spacer(modifier = Modifier.size(20.dp))
//
//                Image(painter = painterResource(R.drawable.baseline_favorite_24),"", modifier = Modifier.size(40.dp))
//                Text(list[index].like_count.toString(), color = Color.White)
//                Spacer(modifier = Modifier.size(10.dp))
//
//                Image(painter = painterResource(R.drawable.baseline_message_24),"", modifier = Modifier.size(40.dp))
//                Text("评论", color = Color.White)
//                Spacer(modifier = Modifier.size(10.dp))
//
//                Image(painter = painterResource(R.drawable.baseline_star_24),"", modifier = Modifier.size(40.dp))
//                val int = SPUtils.getInstance().getInt(list[index].authname)
//
//                Text("收藏", color = Color.White)
//                Spacer(modifier = Modifier.size(10.dp))
//
//                Image(painter = painterResource(R.drawable.baseline_share_24),"", modifier = Modifier.size(40.dp))
//                Text("分享", color = Color.White)
//                Spacer(modifier = Modifier.size(10.dp))
//            }
//        }
//    }

    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        Text("视频", fontWeight = FontWeight.Bold)
//        LazyVerticalStaggeredGrid ( columns = StaggeredGridCells.Fixed(2),
//            verticalItemSpacing = 5.dp,
//            horizontalArrangement = Arrangement.spacedBy(5.dp),
//            modifier = Modifier.fillMaxSize().padding(10.dp)) {
//            items(list){
//                VideoItems(it, navHostController = navHostController)
//            }
//        }

        OutlinedTextField(value = search, onValueChange = {search = it}, modifier = Modifier.fillMaxWidth().padding(0.dp), label = {
            Text("请输入搜索内容")
        }, trailingIcon = {
            Icon(Icons.Default.Search,"", modifier = Modifier.size(30.dp).clickable {
                if (search.isNotEmpty()){
                    vm.sendIntent(VideoIntent.searIn(SearEntity(0,search)))
                    vm.sendIntent(VideoIntent.searQuAll)
                }
            })
        })


        Row {
            Text("搜索记录")
            Spacer(modifier = Modifier.weight(1f))
            Text("清除", modifier = Modifier.clickable {
                vm.sendIntent(VideoIntent.searQuAllDel)
            })
        }


        FlowRow  (modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)){
            list1.forEach {
                searItems(it){
                    vm.sendIntent(VideoIntent.searDel(SearEntity(it.id,it.sear)))
                    vm.sendIntent(VideoIntent.searQuAll)
                }
            }
        }

        LazyColumn (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)){
//            itemsIndexed(list){index: Int, item: VideoEntityItem ->
//                when(index%2){
//                    0->{
//                        VideoItems1(item, navHostController = navHostController)
//                    }
//                    1->{
//                        VideoItems2(item, navHostController = navHostController)
//                    }
//                }
//            }

            items(searList){
                when(it.type){
                    0,2,5->{
                        VideoItems1(it, navHostController = navHostController)
                    }
                    1->{
                        VideoItems2(it, navHostController = navHostController)

                    }
                }
            }
        }

    }

}

@Composable
fun VideoItems(videoEntityItem: VideoEntityItem,navHostController: NavHostController) {
    Card(
        modifier = Modifier.fillMaxSize().padding(5.dp).clickable {
//            SPUtils.getInstance().put("videourl",videoEntityItem.videopath)

            val s = Gson().toJson(videoEntityItem)
            val mmkv = MMKV.defaultMMKV()
            mmkv.encode("video",s)
            navHostController.navigate(Const.VideoPlay)
        },
        colors = CardDefaults.cardColors(Color.White),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // 使用Jetpack Compose的AsyncImage组件异步加载图片
            AsyncImage(
                // 设置图片数据源，这里使用videoEntityItem.videomainimg作为URL
                model = videoEntityItem.videomainimg,
                // 设置图片的描述内容，这里为空字符串
                contentDescription = "",
                // 修饰符，用于设置图片的布局和行为
                modifier = Modifier
                    .fillMaxWidth() // 使图片宽度填满父容器
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)), // 设置图片圆角，左上角和右上角为10dp
                // 设置图片的缩放模式为裁剪，确保图片填满容器并保持比例
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = videoEntityItem.headpath,
                    contentDescription = "",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = videoEntityItem.authname)
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun searItems(it: SearEntity,onLong:()->Unit) {
    Text( text = it.sear, modifier = Modifier.background(Color.Yellow, RoundedCornerShape(10.dp))
        .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)).padding(10.dp,5.dp).combinedClickable(
            onClick = {

            }, onLongClick = {
                onLong()
            }
        ))
}

@Composable
fun VideoItems1(videoEntityItem: VideoEntityItem,navHostController: NavHostController) {
    Card(
        modifier = Modifier.fillMaxSize().padding(10.dp).clickable {
//            SPUtils.getInstance().put("videourl",videoEntityItem.videopath)

            val s = Gson().toJson(videoEntityItem)
            val mmkv = MMKV.defaultMMKV()
            mmkv.encode("video",s)
            navHostController.navigate(Const.VideoPlay)
        },
        colors = CardDefaults.cardColors(Color.White),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = videoEntityItem.headpath,
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = videoEntityItem.authname)
        }
    }
}

@Composable
fun VideoItems2(videoEntityItem: VideoEntityItem,navHostController: NavHostController) {
    Card(
        modifier = Modifier.fillMaxSize().padding(10.dp).clickable {
//            SPUtils.getInstance().put("videourl",videoEntityItem.videopath)

            // 使用Gson库将videoEntityItem对象转换为JSON字符串
            val s = Gson().toJson(videoEntityItem)
            // 获取默认的MMKV实例
            val mmkv = MMKV.defaultMMKV()
            // 将转换后的JSON字符串以"video"为键存储到MMKV中
            mmkv.encode("video",s)
            navHostController.navigate(Const.VideoPlay)
        },
        colors = CardDefaults.cardColors(Color.White),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = videoEntityItem.headpath,
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = videoEntityItem.authname)
        }
    }
}


fun createPlayer(context: Context):ExoPlayer{
    return ExoPlayer.Builder(context).build().apply {
        repeatMode = Player.REPEAT_MODE_ONE
    }
}