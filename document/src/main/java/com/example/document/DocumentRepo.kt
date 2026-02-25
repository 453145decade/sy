package com.example.document

import androidx.compose.runtime.remember
import com.example.lib_base.ApiService
import com.example.lib_base.BaseRepo
import com.example.lib_base.Res
import com.example.lib_http.HttpType
import com.example.lib_http.NetWorkFac
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class DocumentRepo @Inject constructor() : BaseRepo() {

    val apiService by lazy {
        NetWorkFac.factory(HttpType.Video).create(ApiService::class.java)
    }

    /**
     * 上传文件的方法
     * @param loadName String 文件名称
     * @param loadPath String 文件路径
     * @return Flow<Res<Any>> 返回一个Flow流，包含上传结果
     */
    fun upload(loadName:String,loadPath:String):Flow<Res<Any>>{
        // 创建请求体，指定媒体类型为multipart/form-data
        val create = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), File(loadPath))
        // 创建表单数据部分，包含文件名和请求体
        val createFormData = MultipartBody.Part.createFormData("file", loadName, create)
        // 调用API服务上传文件并返回结果Flow
        return apiService.upLoadFile(createFormData)
    }

}