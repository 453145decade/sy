package com.example.lib_base.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lib_base.Entity.SearEntity
import com.example.lib_base.Entity.VideoEntityItem
import com.example.lib_base.Entity.WorkLogEntityItem
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(entities = [WorkLogEntityItem::class,VideoEntityItem::class,SearEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getWorkLogDao():WorkLogDao
    abstract fun getVideoDao():VideoDao
    abstract fun getSearDao():SearDao

//    初始化数据库
    companion object{
        fun init(@ApplicationContext context: Context):AppDataBase{
            val build = Room.databaseBuilder(context, AppDataBase::class.java, "sy.db").build()
            return build
        }
    }



}