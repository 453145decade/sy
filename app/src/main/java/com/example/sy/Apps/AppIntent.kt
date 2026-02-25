package com.example.sy.Apps

import com.example.lib_base.UiIntent
import java.io.File

sealed class AppIntent  : UiIntent{
    data object apps :AppIntent()


    data class download(val url :String,val file: File):AppIntent()
}