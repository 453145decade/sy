package com.example.lib_base.Entity

class VideoComment : ArrayList<VideoCommentItem>()

data class VideoCommentItem(
    val childcount: Int,
    val createtime: String,
    val icon: String,
    val id: Int,
    val msg: String,
    val parentid: Int,
    val rootid: Int,
    val tousername: Any,
    val type: Int,
    val username: String
)