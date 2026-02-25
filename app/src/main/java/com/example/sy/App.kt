package com.example.sy

import android.app.Application
import com.amap.api.location.AMapLocationClient
import com.example.lib_base.BaseApp
import com.tencent.mmkv.MMKV
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App:BaseApp() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)

        AMapLocationClient.updatePrivacyShow(this,true,true)
        AMapLocationClient.updatePrivacyAgree(this,true)

        UMConfigure.init(this, "5a12384aa40fa3551f0001d1", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        PlatformConfig.setQQZone("101830139","5d63ae8858f1caab67715ccd6c18d7a5");

    }
}