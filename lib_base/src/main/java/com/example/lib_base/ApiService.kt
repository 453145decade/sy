package com.example.lib_base

import com.example.lib_base.Entity.AppsEntity
import com.example.lib_base.Entity.ClockEntity
import com.example.lib_base.Entity.GoodsEntity
import com.example.lib_base.Entity.GoodsXQEntity
import com.example.lib_base.Entity.LeaveEntity
import com.example.lib_base.Entity.VideoComment
import com.example.lib_base.Entity.VideoEntity
import com.example.lib_base.Entity.WorkLogEntity
import com.example.lib_base.Entity.CarEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
//    登录
    @POST("/api/user/login")
    fun login(@Body body:RequestBody):Flow<Res<Any>>
//    注册
    @POST("/api/staff/save")
    fun register(@Body body:RequestBody):Flow<Res<Any>>
//    获取应用列表
    @GET("/api/sysapp/all")
    fun getAllApps(): Flow<Res<AppsEntity>>
//    上传
    @Multipart
    @POST("/user/updateIcon")
    fun upLoadFile(@Part file:MultipartBody.Part):Flow<Res<Any>>
//    工作日志
    @GET("/api/oaworklog/all")
    fun getWorkLog():Flow<Res<WorkLogEntity>>
//    添加工作日志
    @POST("/api/oaworklog/save")
    fun addWorkLog(@Body body: RequestBody):Flow<Res<Any>>
//    打卡列表
    @GET("/api/clock/all")
    fun clockList():Flow<Res<ClockEntity>>
//    打卡
    @POST("/api/clock/save")
    fun addClock(@Body body: RequestBody):Flow<Res<Any>>
//    请假
    @GET("/api/leave/page")
    fun leave(@Query("page")page:String,@Query("limit")limit:String):Flow<Res<LeaveEntity>>
//    申请请假
    @POST("/api/leave/save")
    fun addLeave(@Body body: RequestBody):Flow<Res<Any>>
//    审批请假
    @POST("/api/leave/audit")
    fun comLeave(@Body body: RequestBody):Flow<Res<Any>>
//    视频
    @GET("/video/findVideos")
    fun video(@Query("currentPage")currentPage:Int,@Query("pageSize")pageSize:Int):Flow<Res<VideoEntity>>
//    关注
    @POST("/guanzhu/guanzhu")
    fun GZVideo(@Body body: RequestBody):Flow<Res<Any>>
//    取消关注
    @POST("/guanzhu/noguanzhu")
    fun QGVideo(@Body body: RequestBody):Flow<Res<Any>>
//    视频评论
    @GET("/comment/getCommentByVideoId")
    fun videoComment(@Query("videoId")videoId:Int):Flow<Res<VideoComment>>
//    发布评论
    @POST("/comment/comment")
    fun SendVideoComment(@Body body: RequestBody):Flow<Res<Any>>
//    商品列表
    @GET("/goods/info")
    fun goods(
        @Query("category_id")category_id:Int,
        @Query("currentPage")currentPage:Int,
        @Query("pageSize")pageSize:Int
    ):Flow<Res<GoodsEntity>>
//    商品详情
    @GET("/goods/detail")
    fun goodsXq(@Query("goods_id")goods_id:Int):Flow<Res<GoodsXQEntity>>
//    添加购物车
    @POST("/goods/addCar")
    fun addCar(@Body body: RequestBody):Flow<Res<Any>>
//    购物车列表
    @GET("/goods/selectCar")
    fun carList():Flow<Res<CarEntity>>

}