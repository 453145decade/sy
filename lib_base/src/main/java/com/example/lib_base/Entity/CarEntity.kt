package com.example.lib_base.Entity

class CarEntity : ArrayList<CarEntityItem>()

data class CarEntityItem(
    val count: Int,
    val goods_default_icon: String,
    val goods_default_price: Int,
    val goods_desc: String,
    val goods_id: Int,
    val id: Int,
    val order_id: Int,
    val user_id: Int,
    var isChecked : Boolean
)