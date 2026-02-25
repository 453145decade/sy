package com.example.sy.Video

import com.example.lib_base.ApiService
import com.example.lib_base.BaseRepo
import com.example.lib_base.Entity.SearEntity
import com.example.lib_base.Entity.VideoComment
import com.example.lib_base.Entity.VideoEntity
import com.example.lib_base.Entity.VideoEntityItem
import com.example.lib_base.Res
import com.example.lib_base.Room.SearDao
import com.example.lib_base.Room.VideoDao
import com.example.lib_http.HttpType
import com.example.lib_http.NetWorkFac
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VideoRepo @Inject constructor(var videoDao: VideoDao,var searDao: SearDao):BaseRepo(){
    val apiService by lazy {
        NetWorkFac.factory(HttpType.Video).create(ApiService::class.java)
    }
    fun video( currentPage:Int, pageSize:Int):Flow<Res<VideoEntity>> {
        return apiService.video(currentPage,pageSize)
    }
    fun videoGZ(map: Map<String,Any>):Flow<Res<Any>>{
        return apiService.GZVideo(toBody(map))
    }
    fun videoQG(map: Map<String,Any>):Flow<Res<Any>>{
        return apiService.QGVideo(toBody(map))
    }
    fun videoComment(videoId:Int):Flow<Res<VideoComment>>{
        return apiService.videoComment(videoId)
    }
    fun sendVideoComment(map: Map<String, Any>):Flow<Res<Any>>{
        return apiService.SendVideoComment(toBody(map))
    }

    fun videoIn(videoEntityItem: VideoEntityItem):Flow<Any>{
         return flow {
             emit(videoDao.insertVideo(videoEntityItem))
         }
    }
    fun videoQuAll():Flow<List<VideoEntityItem>>{
        return flow {
            emit(videoDao.queryVideo())
        }
    }
    fun videoDel(videoEntityItem: VideoEntityItem):Flow<Any>{
         return flow {
             emit(videoDao.deleteVideo(videoEntityItem))
         }
    }

    fun searIn(searEntity: SearEntity):Flow<Any>{
         return flow {
             emit(searDao.insertSear(searEntity))
         }
    }
    fun searQuAll():Flow<List<SearEntity>>{
        return flow {
            emit(searDao.queryAllSear())
        }
    }
    fun searDel(searEntity: SearEntity):Flow<Any>{
         return flow {
             emit(searDao.deleteSear(searEntity))
         }
    }
    fun searDelAll():Flow<Any>{
         return flow {
             emit(searDao.deleteAllSear())
         }
    }
}