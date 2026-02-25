package com.example.lib_base.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

class WorkLogEntity : ArrayList<WorkLogEntityItem>()

@Entity(tableName = "workLog")
data class WorkLogEntityItem(
    val content: String,
    val createTime: String,
    val deptName: String,
    @PrimaryKey
    val id: Int,
    val staffName: String,
    val status: String,
    val title: String,
    val uid: Int,
    val updateTime: String,
    val workDate: String?
)