package com.example.sy.Apps

import com.example.lib_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(var appRepo: AppRepo):BaseViewModel<AppIntent>()  {
    override fun handleIntent(it: AppIntent) {
        when(it){
            AppIntent.apps -> {
                httpRequest( appRepo.getapps())
            }

            is AppIntent.download -> {
                download(it.url,it.file)
            }
        }
    }
}