package com.example.document

import com.example.lib_base.UiIntent
import java.io.File

sealed class DocumentIntent : UiIntent{
    data class ScanLocalFile(val file:File):DocumentIntent()

    data class UpLoadFile(val fileName:String,val filePath:String):DocumentIntent()
}