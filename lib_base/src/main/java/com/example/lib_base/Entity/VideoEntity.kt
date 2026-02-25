package com.example.lib_base.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

class VideoEntity : ArrayList<VideoEntityItem>()

@Entity(tableName = "video")
data class VideoEntityItem(
    val address: String ,
    val authname: String,
    val caption: String,
    val dianzan: Int,
    val guanzhu: Int,
    val headpath: String,
    @PrimaryKey
    val id: Int,
    val like_count: Int,
    val publishtime: String,
    val type: Int,
    val videomainimg: String,
    val videopath: String,
    val view_count: Int
)