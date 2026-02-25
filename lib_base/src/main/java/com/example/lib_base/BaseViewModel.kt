package com.example.lib_base

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.Request
import java.io.File

abstract class BaseViewModel<uiIntent:UiIntent>:ViewModel() {
    private var channel = Channel<uiIntent> {Channel.UNLIMITED }
    private var _stare = MutableStateFlow<UiStare>(UiStare.onLoad)

    val uiStare : StateFlow<UiStare>
        get() = _stare

    fun sendIntent(uiIntent: uiIntent){
        viewModelScope.launch {
            channel.send(uiIntent)
        }
    }
    init {
        viewModelScope.launch {
            channel.receiveAsFlow().collect{
                handleIntent(it)
            }
        }
    }

    abstract fun handleIntent(it: uiIntent)


    fun <T> httpRequest(request:Flow<Res<T>>,stateType: StateType=StateType.DEFAULT){
        viewModelScope.launch {
            request.flowOn(Dispatchers.IO)
                .catch {
                    Log.d("", it.message.toString())
                }
                .collect{
                    if(it.code == 0||it.code==200){
                        _stare.value = UiStare.onSuccess(it.data,stateType)
                    }else{
                        if (it.msg!=null){
                            _stare.value = UiStare.onFail(it.msg)
                        }else {
                            _stare.value = UiStare.onFail(it.message)
                        }
                    }

                }
        }
    }

    //数据库
    fun roomRequest(request: Flow<Any>,stateType: StateType=StateType.DEFAULT){
        viewModelScope.launch{
            request.flowOn(Dispatchers.IO)
                .catch { Log.e("ljroom","数据库请求失败${it.message}") }
                .collect{
                    if (it!=null){
                        _stare.value= UiStare.onSuccess(it,stateType)
                    }else{
                        _stare.value=UiStare.onFail("数据库查询失败")
                    }
                }
        }
    }

    fun download(url:String,file: File){
        viewModelScope.launch {
            DownloadUtils.download(url,file)
                .flowOn(Dispatchers.IO).catch {
                Log.e("ljdownload","下载失败${it.message}")
            }.collect{
                _stare.value=it
            }
        }
    }

    fun scanLocalFile(file:File){
        viewModelScope.launch {
            try {
// 获取文件对象下的所有文件和目录，返回文件数组
                val listFiles = file.listFiles()
// 创建一个可变的FileEntity类型列表，用于存储文件信息
                val list = mutableListOf<FileEntity>()

// 遍历文件列表，为每个文件创建FileEntity对象并添加到list集合中
                listFiles.forEach {
    // 使用文件名和绝对路径创建FileEntity对象，并添加到list集合
                    list.add(FileEntity(it.name,it.absolutePath))
                }
// 更新_stare的状态值为成功状态，包含处理后的文件列表和默认状态类型
                _stare.value = UiStare.onSuccess(list,StateType.DEFAULT)

            }catch (e:Exception){
// 更新_stare的状态为失败状态，并将异常信息传递给UiStare.onFail
                _stare.value = UiStare.onFail(e.message.toString())
            }
        }
    }

}


data class FileEntity(
    val name:String,
    val absolutePath:String,
)