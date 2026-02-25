package com.example.lib_base

sealed class UiStare {
    data class onSuccess<T>(val data:T,val stateType:StateType):UiStare()
    data class onFail(val mag:String):UiStare()
    data object onLoad:UiStare()
}