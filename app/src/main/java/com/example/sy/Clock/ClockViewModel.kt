package com.example.sy.Clock

import com.example.lib_base.BaseViewModel
import com.example.lib_base.StateType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(var clockRepo: ClockRepo):BaseViewModel<ClockIntent>() {
    override fun handleIntent(it: ClockIntent) {
        when(it){
            ClockIntent.clockList -> {
                httpRequest(clockRepo.clockList())
            }

            is ClockIntent.addClock -> {
                 httpRequest(clockRepo.addClock(it.map))
            }
        }
    }
}