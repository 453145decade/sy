package com.example.sy.Goods

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.lib_base.Const
import com.example.lib_base.Entity.CarEntity
import com.example.lib_base.Entity.CarEntityItem
import com.example.lib_base.StateType
import com.example.lib_base.UiStare
import kotlinx.coroutines.launch

@Composable
fun CarPage(navHostController: NavHostController,vm:GoodsViewModel = hiltViewModel()) {

    var list = remember {
        mutableStateListOf<CarEntityItem>()
    }

    var isAll by remember {
        mutableStateOf(false)
    }

    LaunchedEffect("") {
        launch {
            vm.uiStare.collect{
                when(it){
                    is UiStare.onFail -> {

                    }
                    UiStare.onLoad -> {

                    }
                    is UiStare.onSuccess<*> -> {
                        when(it.stateType){
                            StateType.DEFAULT -> {
                                list.clear()
                                list.addAll(it.data as CarEntity)
                            }
                            StateType.DOWNLOAD -> {

                            }
                            StateType.Scan -> {

                            }
                            StateType.INSERT -> {

                            }
                            StateType.del -> {

                            }
                            StateType.comment -> {

                            }
                            StateType.send -> {

                            }
                        }
                    }
                }
            }
        }
        vm.sendIntent(GoodsIntent.carList)
    }

    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        Row {
            // 复选框组件，用于全选/取消全选操作
            // checked: 绑定复选框的选中状态，isAll为全选状态变量
            // onCheckedChange: 复选框状态改变时的回调函数
            Checkbox(checked = isAll, onCheckedChange = {
                // 计算新的选中状态，取当前isAll的反值
                var newsIsChecked = !isAll
                // 遍历列表中的每个元素，更新其选中状态
                list.forEachIndexed { index, carEntityItem ->
                    // 创建新的列表元素，更新isChecked属性为新的选中状态
                    list[index] = carEntityItem.copy(isChecked = newsIsChecked)
                }
                // 更新全选状态变量
                isAll = newsIsChecked
            })
            Text(text = "全选")
        }
        LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
            // 遍历列表中的每个元素
            items(list) {item->
                // 渲染每个车辆项，并处理选中状态变化
                carItem(item, onCheck = {isChecked->
// 使用 indexOfFirst 函数查找元素在列表中的索引
// 该函数接受一个 lambda 表达式作为条件判断
                    val index = list.indexOfFirst {
    // 在 lambda 表达式中比较当前项的 id 与列表项的 id 是否相等
    // 如果找到第一个匹配的项，则返回该位置的索引
                        item.id == it.id
                    }
                    // 检查index是否不等于-1，确保index有效
                    if (index != -1){
                        // 使用copy函数创建一个新的item对象，仅更新isChecked属性
                        // 然后将这个新对象赋值给list中对应index的位置
                        list[index] = item.copy(isChecked = isChecked)
                    }
                    //判断是否全选
// 使用 all 函数检查列表中所有元素是否满足指定条件
// all 是 Kotlin 集合中的一个高阶函数，用于判断集合中是否所有元素都满足给定的条件
// 如果所有元素都满足条件，则返回 true；否则返回 false
                    isAll = list.all {
    // 这里使用了一个 lambda 表达式作为 all 函数的参数
    // it 是默认的参数名，代表列表中的当前元素
    // it.isChecked 表示检查当前元素的 isChecked 属性是否为 true
                        it.isChecked
                    }
                })
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "合计 : 0.00")

            Button(onClick = {
                navHostController.navigate(Const.PayPage)
            }) {
                Text(text = "结算")
            }
        }
    }


}

@Composable
fun carItem(it: CarEntityItem,onCheck:(Boolean)->Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row (modifier = Modifier.fillMaxWidth(), verticalAlignment =Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Checkbox(checked = it.isChecked, onCheckedChange = onCheck )
            AsyncImage(model = it.goods_default_icon,"", modifier = Modifier.size(50.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = it.goods_desc, maxLines = 1, fontSize = 14.sp)
                Text(text = "单价 : "+it.goods_default_price.toString(), maxLines = 1, fontSize = 14.sp)
                Text(text = "数量 : "+it.count.toString(), maxLines = 1, fontSize = 14.sp)
            }
        }
        Spacer(modifier = Modifier.size(5.dp))
        Divider(thickness = 1.dp)
        Spacer(modifier = Modifier.size(5.dp))
    }
}
