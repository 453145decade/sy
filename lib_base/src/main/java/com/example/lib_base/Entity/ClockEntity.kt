package com.example.lib_base.Entity

class ClockEntity : ArrayList<ClockEntityItem>()

data class ClockEntityItem(
    val address: String,
    val clockTime: String,
    val createTime: String,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val status: String,
    val type: String,
    val uid: Int
)