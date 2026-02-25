package com.example.sy.WorkLog

import com.example.lib_base.Entity.WorkLogEntityItem
import com.example.lib_base.UiIntent

sealed class WorkLogIntent:UiIntent {
    data object workLogList:WorkLogIntent()
    data class addWorkLog(val map: Map<String,Any>):WorkLogIntent()

    data class StoreReport(val scWorkLogEntityItem: WorkLogEntityItem):WorkLogIntent()
    data object QueryAllReport:WorkLogIntent()

    data class del(val scWorkLogEntityItem: WorkLogEntityItem):WorkLogIntent()
}