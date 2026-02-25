package com.example.lib_base.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sear")
data class SearEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var sear:String
)
