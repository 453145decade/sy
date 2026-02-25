package com.example.sy.Video

import com.example.lib_base.Entity.SearEntity
import com.example.lib_base.Entity.VideoEntityItem
import com.example.lib_base.UiIntent

sealed class VideoIntent : UiIntent {
    data class video(var currentPage:Int,var pageSize:Int):VideoIntent()
    data class videoGZ(var map: Map<String,Any>):VideoIntent()
    data class videoQG(var map: Map<String,Any>):VideoIntent()
    data class videoComment(var videoId:Int):VideoIntent()
    data class sendVideoComment(var map: Map<String, Any>):VideoIntent()

    data class videoIn(var videoEntityItem: VideoEntityItem):VideoIntent()
    data class videoDel(var videoEntityItem: VideoEntityItem):VideoIntent()
    data object videoQuAll : VideoIntent()

    data class searIn(var searEntity: SearEntity):VideoIntent()
    data class searDel(var searEntity: SearEntity):VideoIntent()
    data object searQuAll : VideoIntent()
    data object searQuAllDel : VideoIntent()


}