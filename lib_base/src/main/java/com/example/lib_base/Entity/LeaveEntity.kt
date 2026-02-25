package com.example.lib_base.Entity

class LeaveEntity : ArrayList<LeaveEntityItem>()

data class LeaveEntityItem(
    val auditId: Int,
    val comReason: String,
    val comTime: String,
    val createTime: String,
    val deptName: String,
    val endTime: String,
    val hours: Int,
    val id: Int,
    val reason: String,
    val sid: Int,
    val staffName: String,
    val startTime: String,
    val status: String,
    val type: String,
    val uid: Int,
    val updateTime: String
)