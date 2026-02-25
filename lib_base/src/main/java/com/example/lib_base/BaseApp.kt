package com.example.lib_base

import android.app.Application
import android.content.Context

open class BaseApp:Application() {
    companion object{
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}