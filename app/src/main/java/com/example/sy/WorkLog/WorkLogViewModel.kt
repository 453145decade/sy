package com.example.sy.WorkLog

import com.example.lib_base.BaseViewModel
import com.example.lib_base.StateType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkLogViewModel @Inject constructor(var workLogRepo: WorkLogRepo):BaseViewModel<WorkLogIntent>() {
    override fun handleIntent(it: WorkLogIntent) {
        when(it){
            is WorkLogIntent.workLogList -> {
                httpRequest(workLogRepo.getWorkLog())
            }

            is WorkLogIntent.addWorkLog -> {
                httpRequest(workLogRepo.addWorkLog(it.map))
            }

            is WorkLogIntent.QueryAllReport -> {
                roomRequest(workLogRepo.queryAllReport())
            }
            is WorkLogIntent.StoreReport -> {
                roomRequest(workLogRepo.storeReport(it.scWorkLogEntityItem),StateType.INSERT)
            }

            is WorkLogIntent.del -> {
                roomRequest(workLogRepo.del(it.scWorkLogEntityItem),StateType.del)
            }
        }
    }
}