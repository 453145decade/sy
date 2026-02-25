package com.example.document

import com.example.lib_base.BaseViewModel
import com.example.lib_base.StateType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(private val documentRepo: DocumentRepo):BaseViewModel<DocumentIntent>() {
    override fun handleIntent(it: DocumentIntent) {
        when(it){
            is DocumentIntent.ScanLocalFile -> {
                scanLocalFile(it.file)
            }

            is DocumentIntent.UpLoadFile -> {
                httpRequest(documentRepo.upload(it.fileName,it.filePath),StateType.Scan)
            }
        }
    }
}