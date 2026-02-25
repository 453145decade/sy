package com.example.lib_base.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lib_base.Entity.WorkLogEntityItem

@Dao
interface WorkLogDao {
//    添加
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorkLog(scWorkLogEntityItem: WorkLogEntityItem)

//    查找
    @Query("select *from workLog")
    fun queryAllWorkLog():MutableList<WorkLogEntityItem>

//    单个查找
    @Query("select *from workLog where id=:id")
    fun queryWorkLog(id:Int):WorkLogEntityItem

//    删除
    @Delete
    fun deleteWorkLog(scWorkLogEntityItem: WorkLogEntityItem)

//    修改
    @Update
    fun updateWorkLog(scWorkLogEntityItem: WorkLogEntityItem)
}