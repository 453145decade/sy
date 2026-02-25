package com.example.sy.Clock

import com.example.lib_base.UiIntent

sealed class ClockIntent : UiIntent {
    data object clockList : ClockIntent()
    data class addClock(val map: Map<String,Any>) : ClockIntent()

}