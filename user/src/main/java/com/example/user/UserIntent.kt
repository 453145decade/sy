package com.example.user

import com.example.lib_base.UiIntent

sealed class UserIntent : UiIntent {
    data class login(val map: Map<String,Any>):UserIntent()
    data class register(val map: Map<String,Any>):UserIntent()
}