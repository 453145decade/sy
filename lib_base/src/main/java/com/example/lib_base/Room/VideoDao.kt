package com.example.lib_base.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.lib_base.Entity.VideoEntityItem

@Dao
interface VideoDao {
//    添加
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideo( videoEntityItem: VideoEntityItem)
//    查询
    @Query("select * from video")
    fun queryVideo(): MutableList<VideoEntityItem>
//    删除
    @Delete()
    fun deleteVideo(videoEntityItem: VideoEntityItem)
//    查找单个
    @Query("select * from video where id = :id")
    fun queryVideoById(id: Int): VideoEntityItem

}