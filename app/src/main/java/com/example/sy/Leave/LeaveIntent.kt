package com.example.sy.Leave

import com.example.lib_base.UiIntent

sealed class LeaveIntent : UiIntent {
    data class leave(var page:String,var limit:String):LeaveIntent()
    data class addLeave(var map: Map<String,Any>):LeaveIntent()
    data class comLeave(var map: Map<String,Any>):LeaveIntent()
}