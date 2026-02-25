package com.example.sy.WorkLog

import com.example.lib_base.ApiService
import com.example.lib_base.BaseRepo
import com.example.lib_base.Entity.WorkLogEntity
import com.example.lib_base.Entity.WorkLogEntityItem
import com.example.lib_base.Res
import com.example.lib_base.Room.WorkLogDao
import com.example.lib_http.HttpType
import com.example.lib_http.NetWorkFac
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WorkLogRepo @Inject constructor(private val workLogDao: WorkLogDao):BaseRepo() {
    private val apiService by lazy {
        NetWorkFac.factory(HttpType.Token).create(ApiService::class.java)
    }

    fun getWorkLog():Flow<Res<WorkLogEntity>>{
        return  apiService.getWorkLog()
    }
    fun addWorkLog(map: Map<String,Any>):Flow<Res<Any>>{
        return apiService.addWorkLog(toBody(map))
    }


    fun storeReport(scWorkLogEntityItem: WorkLogEntityItem):Flow<Any>{
        return flow {
            emit(workLogDao.insertWorkLog(scWorkLogEntityItem))
        }
    }

    fun queryAllReport():Flow<List<WorkLogEntityItem>>{
        return flow {
            emit(workLogDao.queryAllWorkLog())
        }
    }

    fun del(scWorkLogEntityItem: WorkLogEntityItem):Flow<Any>{
        return flow {
            emit(workLogDao.deleteWorkLog(scWorkLogEntityItem))
        }
    }
}