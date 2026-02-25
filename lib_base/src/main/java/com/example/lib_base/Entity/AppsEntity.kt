package com.example.lib_base.Entity

class AppsEntity : ArrayList<AppsEntityItem>()

data class AppsEntityItem(
    val apkUrl: String,
    val appIcon: String,
    val appId: Int,
    val appImgs: String,
    val appIntroduction: String,
    val appName: String,
    val appPackage: String,
    val appSize: Int,
    val appType: String,
    val appUserId: Int,
    val createTime: String,
    val netUrl: String,
    val operationFlag: Boolean,
    val trustFlag: Boolean,
    val updateStr: String,
    val updateTime: String,
    val uploadTime: Any,
    val versionCode: String
)