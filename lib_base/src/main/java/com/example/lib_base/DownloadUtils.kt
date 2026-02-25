package com.example.lib_base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

object DownloadUtils {
    fun download(url:String,file: File): Flow<UiStare> {
        return flow {
            val build = Request.Builder().url(url).build()
            val execute = OkHttpClient.Builder().build().newCall(build).execute()
            if (execute.isSuccessful){
                execute.body.let { body->
                    file.outputStream().let { os->
                        //记录字节长度
                        var len=0
                        //从body中取出字节
                        val bs=body?.byteStream()
                        //字节数组
                        val ba=ByteArray(DEFAULT_BUFFER_SIZE)
                        while (bs?.read(ba).also { len=it!! }!=-1){
                            os.write(ba,0,len)
                        }
                        os.flush()
                        os.close()
                    }
                }
                emit(UiStare.onSuccess("下载成功",StateType.DOWNLOAD))
            }else{
                emit(UiStare.onFail("下载失败"))
            }
        }
    }
}