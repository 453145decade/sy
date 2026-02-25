package com.example.user.Page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.blankj.utilcode.util.ToastUtils
import com.example.lib_base.Const
import com.example.lib_base.UiStare
import com.example.user.R
import com.example.user.UserIntent
import com.example.user.UserViewModel
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
data class User(val no:String,val phone:String)
@Composable
fun RegisterPage(navHostController: NavHostController,vm:UserViewModel= hiltViewModel()) {

    var name by remember {
        mutableStateOf("")
    }
    var no by remember {
        mutableStateOf("")
    }
    var phone by remember {
        mutableStateOf("")
    }
    var did by remember {
        mutableStateOf("")
    }
    var pid by remember {
        mutableStateOf("")
    }
    var pname by remember {
        mutableStateOf("")
    }
    var isshop by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = "") {
        launch {
            vm.uiStare.collect{
                when(it){
                    is UiStare.onFail -> {
                        ToastUtils.showShort(it.mag.toString())
                    }
                    UiStare.onLoad -> {

                    }
                    is UiStare.onSuccess<*> -> {
                        ToastUtils.showShort("注册成功")

                        navHostController.popBackStack()

                    }
                }
            }
        }
    }
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
        Text("注册")
        //*******
        OutlinedTextField(value = name, onValueChange = {name=it},
            label = { Text("输入注册昵称") },
            leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = "") },
            trailingIcon = { Icon(Icons.Default.Lock, contentDescription = "") },
            modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = no, onValueChange = {no=it},
            label = { Text("输入注册账号") },
            leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = "")},
            trailingIcon = { Icon(Icons.Default.Lock,
                contentDescription = "", modifier = Modifier
                    .clickable { isshop = !isshop }) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(value = phone, onValueChange = {phone=it},
            label = { Text("输入注册密码") },
            leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = "")},
            trailingIcon = { Icon(painterResource(id = if (isshop) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_remove_red_eye_24),
                contentDescription = "", modifier = Modifier
                    .clickable { isshop = !isshop }) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isshop) VisualTransformation.None else PasswordVisualTransformation()
        )
        OutlinedTextField(value = did, onValueChange = {did=it}, label = {
            Text(text = "请输入注册的部门id")
        }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = pid, onValueChange = {pid=it}, label = {
            Text(text = "请输入注册的职位id")
        }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = pname, onValueChange = {pname=it}, label = {
            Text(text = "请输入注册的职位名称")
        }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            //非空校验
            if (name.isNullOrEmpty()||no.isNullOrEmpty()||phone.isNullOrEmpty()||did.isNullOrEmpty()||pid.isNullOrEmpty()||pname.isNullOrEmpty()){
                ToastUtils.showLong("输入框不能为空")
                return@Button
            }

            if (phone.length<6){
                ToastUtils.showLong("密码长度有问题")
                return@Button
            }
            //用户名是由数字，字母组成，数字和字母至少个包含一位，4-8为
            val regex="^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*_)[a-zA-Z0-9_]{6,12}$".toRegex()
            if (!phone.matches(regex)){
                ToastUtils.showLong("密码不符合要求")
                return@Button
            }
            val map= mapOf(
                Const.regname to name,
                Const.regno to no,
                Const.regphone to phone,
                Const.regdid to did,
                Const.regpid to pid,
                Const.regpname to pname
            )
            vm.sendIntent(UserIntent.register(map))
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "注册")
        }
    }
}