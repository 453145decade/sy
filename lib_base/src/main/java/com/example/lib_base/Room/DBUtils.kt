package com.example.lib_base.Room

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBUtils {

//    提供数据库对象
    @Provides
    @Singleton
    fun initDB(@ApplicationContext context: Context):AppDataBase{
        return AppDataBase.init(context)
    }

//    对外暴露
    @Provides
    fun getWorkLogDao(appDataBase: AppDataBase):WorkLogDao{
        return appDataBase.getWorkLogDao()
    }

    @Provides
    fun getWorkLogDao2(appDataBase: AppDataBase):VideoDao{
        return appDataBase.getVideoDao()
    }
    @Provides
    fun getSearDao(appDataBase: AppDataBase):SearDao{

        return appDataBase.getSearDao()
    }
}