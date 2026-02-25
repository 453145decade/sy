package com.example.sy.Leave

import com.example.lib_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LeaveViewModel @Inject constructor(var leaveRepo: LeaveRepo):BaseViewModel<LeaveIntent>() {
    override fun handleIntent(it: LeaveIntent) {
        when(it){
            is LeaveIntent.leave -> {
                httpRequest(leaveRepo.leave(it.page,it.limit))
            }

            is LeaveIntent.addLeave -> {
                httpRequest(leaveRepo.addLeave(it.map))
            }

            is LeaveIntent.comLeave -> {
                httpRequest(leaveRepo.comLeave(it.map))
            }
        }
    }
}