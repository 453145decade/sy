package com.example.user

import com.example.lib_base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepo: UserRepo):BaseViewModel<UserIntent>() {
    override fun handleIntent(it: UserIntent) {
        when(it){
            is UserIntent.login -> {
                httpRequest(userRepo.login(it.map))
            }

            is UserIntent.register -> {
                httpRequest(userRepo.register(it.map))
            }
        }
    }

}