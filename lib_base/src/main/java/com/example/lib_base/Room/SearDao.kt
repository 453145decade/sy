package com.example.lib_base.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lib_base.Entity.SearEntity

@Dao
interface SearDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSear(searEntity: SearEntity)
    @Query("select * from  sear")
    fun queryAllSear(): MutableList<SearEntity>
    @Delete
    fun deleteSear(searEntity: SearEntity)
//    删除全部
    @Query("DELETE FROM sear")
    fun deleteAllSear()

}