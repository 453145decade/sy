package com.example.sy.Video

import com.example.lib_base.BaseViewModel
import com.example.lib_base.StateType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(var videoRepo: VideoRepo):BaseViewModel<VideoIntent>() {
    override fun handleIntent(it: VideoIntent) {
        when(it){
            is VideoIntent.video -> {
                httpRequest(videoRepo.video(it.currentPage,it.pageSize))
            }

            is VideoIntent.videoGZ -> {
                httpRequest(videoRepo.videoGZ(it.map), stateType = StateType.DOWNLOAD)
            }

            is VideoIntent.videoQG -> {
                httpRequest(videoRepo.videoQG(it.map), stateType = StateType.Scan)
            }


            is VideoIntent.videoDel -> {
                roomRequest(videoRepo.videoDel(it.videoEntityItem))
            }
            is VideoIntent.videoIn -> {

            }
            VideoIntent.videoQuAll -> {

            }

            is VideoIntent.videoComment -> {
                httpRequest(videoRepo.videoComment(it.videoId), stateType = StateType.comment)
            }

            is VideoIntent.sendVideoComment -> {
                httpRequest(videoRepo.sendVideoComment(it.map), stateType = StateType.send)
            }

            is VideoIntent.searDel -> {
                roomRequest(videoRepo.searDel(it.searEntity), stateType = StateType.del)
            }
            is VideoIntent.searIn -> {
                roomRequest(videoRepo.searIn(it.searEntity), stateType = StateType.INSERT)
            }
            VideoIntent.searQuAll -> {
                roomRequest(videoRepo.searQuAll(), stateType = StateType.Scan)
            }
            VideoIntent.searQuAllDel -> {
                roomRequest(videoRepo.searDelAll(), stateType = StateType.send)
            }
        }
    }
}