package com.example.user.Page

import android.widget.CheckBox
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun LoginPage(vm:UserViewModel = hiltViewModel(),navHostController: NavHostController) {
    var name by remember {
        mutableStateOf("")
    }
    var pw by remember {
        mutableStateOf("")
    }
    var role by remember {
        mutableStateOf("经理")
    }
    var showPw by remember {
        mutableStateOf(false)
    }
    var isChecked by remember {
        mutableStateOf(false)
    }

    // 使用LaunchedEffect来启动一个协程，当key1变化时重新执行
    // 这里key1设置为空字符串，表示这个effect只会在组件首次进入组合时执行一次
    LaunchedEffect(key1 = "") {
        // 使用launch启动一个新的协程
        launch {
            vm.uiStare.collect{
                when(it){
                    is UiStare.onFail -> {

                    }
                    UiStare.onLoad -> {

                    }
                    is UiStare.onSuccess<*> -> {
                        ToastUtils.showLong("登录成功")
                        navHostController.popBackStack()
                        val mmkv = MMKV.defaultMMKV()

                        mmkv.encode(Const.Token,it.data.toString())
                        mmkv.encode(Const.logName,name)
                        mmkv.encode(Const.logRole,role)

                    }
                }
            }
        }
    }


    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
        OutlinedTextField(value = name, onValueChange = {name = it}, leadingIcon = {
            Icon(imageVector = Icons.Default.AccountCircle,"")
        }, label = {
            Text("请输入登录用户名")
        }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = pw, onValueChange = {pw = it}, leadingIcon = {
            Icon(painter = painterResource(if (showPw) R.drawable.baseline_remove_red_eye_24 else R.drawable.baseline_visibility_off_24),
                contentDescription = "", modifier = Modifier.clickable {
                    showPw = !showPw
                })
        }, label = {
            Text("请输入密码")
        }, modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showPw) VisualTransformation.None else PasswordVisualTransformation()
        )


        Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)){
            Text("角色")
            Checkbox(checked = role=="经理", onCheckedChange = {
                role = "经理"
            })
            Text("经理")
            Checkbox( checked = role == "员工", onCheckedChange = {
                role = "员工"
            })
            Text("员工")
        }


        Row (verticalAlignment = Alignment.CenterVertically) {

            Checkbox(checked = isChecked, onCheckedChange = {
                isChecked = !isChecked
            })
            Text("我已经阅读并同意《用户协议》和隐私政策")
        }

        Button(onClick = {
//            点击登录
            if (name.isNullOrEmpty() || pw.isNullOrEmpty()){
                ToastUtils.showLong("请先输入用户或密码")
                return@Button
            }
            if (!isChecked){
                ToastUtils.showLong("请先勾选用户协议")
                return@Button
            }
            val map = mapOf(
// 使用 to 操作符创建键值对对
// 第一对：键为 Const.logName，值为 name
                Const.logName to name,
// 第二对：键为 Const.logPw，值为 pw
                Const.logPw to pw
            )
            vm.sendIntent(UserIntent.login(map))

        }, modifier = Modifier.fillMaxWidth()) {
            Text("登录")
        }

        Text(text = "没有账号？去注册", modifier = Modifier.clickable {
            navHostController.navigate(Const.registerPage)
        })

    }

}