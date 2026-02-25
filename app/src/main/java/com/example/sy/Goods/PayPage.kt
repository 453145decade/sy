package com.example.sy.Goods

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.alipay.sdk.app.EnvUtils
import com.alipay.sdk.app.PayTask
import com.example.lib_base.BaseApp
import com.example.sy.pay.PayResult
import com.example.sy.pay.util.OrderInfoUtil2_0
import java.util.Random


/**
 * 用于支付宝支付业务的入参 app_id。
 */

const val APPID = "2021000118655415"

/**
 * 用于支付宝账户登录授权业务的入参 pid。
 */

const val PID = ""

/**
 * 用于支付宝账户登录授权业务的入参 target_id。
 */

const val TARGET_ID = ""

/**
 * pkcs8 格式的商户私钥。
 *
 *
 * 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
 * 使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
 * RSA2_PRIVATE。
 *
 *
 * 建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
 * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
 */

const val RSA2_PRIVATE =
    "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCpf4lEigXwChQW6OPYC4lhgBNOI25JVMgnr0DWHHfSha3SqskPDCGs/Qk8f4WVjregCgbiD6sAX4UBi50srJULiDytlKjyKVhmEe4ZTw3xG/cJ/lfQZ2/aC46tD5eFEXDh/7shGiQEflDZBXJ4amaicBCNTcISz6fxaRWtab5DbWqMvZBX2xOr0gUzu6l1RE//tXMtyJD3yyVM6/A5zIeReqSfzrok1ZAW1GhRLsvZJPpvY+v95UE2fXUt8y3piFxvheRGQ7I5RVNW4/8TGJ7RemnNOyaT3c/aDnNUNq/wKFZ0UAm8rf2/WYeHcLATXIjk3wSl0LzehEG6l3qTabeFAgMBAAECggEAYToIKmml+Eg/t9iFgdM0TdpiuIq4Y5YfbiwoW+lejkMiL1rKFDiYRJutJafcn6qLCGFYC2qDY6ZnLDjCwvKIWGIgtE03EBnJXFtiod/oFms9LN1Zz/DVh1Tj1b/A4ZLclrbTx6wwBufSRnrKa+pUz07LH9L/xTyakG8AAUZL3m693DRUXbvC7QdzBaIWY6wvVMM/3Gv4L2KK/dd7DOTzzkn1GB+g1BNYtJW1LuZUUDkEYKaHps98U+rH0xf63AlRRdjCp1la8Wso+9y2B+Wrrg3QpTzmwVXo4f2nQgYTNhyD3XTN/scN+vSb28UyS4EnGBv89UmtaNc8bKAVaF4IDQKBgQD1j/fznQt4iPdubS2w5FcdP2cXqj0EC0S2+M4ncYeimN/Ag0kZnFz7rVFoYfX6RFLlxtRpWjN764+rHpO6mTdNdWFSbqrFj3yXtomcJzGtERPrPB6zT2Gy60UjPh0I5sC9QX+NMtVkxlAdfw5BBPbLfksUeSQcjXizLF2S/GlLJwKBgQCws+RzJ9WrRRrAFwgjYzxgIcU+wiYIca91Ew8vVAT1no+ayjwIvQOaVI/g7OczuFhRWXV1+kzcyJ9+AmGRkAqU/hCnjQ62Eh2z3PLUsfP0MrnzRcAaWAR9r9ANhd+m6bNBVsP10Q/57+oapgmNWSG2gCekTg0w4nhqiiMJhRiDcwKBgQCnquGWSUinyH2JvKpR+dcxdTUjB24Z8Me+VywL6oy+DLvaasni3szuW20WZKzbcQNZOOWX+arllfXG1V3xLuR/0wHckwuvUUW1IUHciTZ2AQ8ksywTKsR+sf3HDoYsqcI4dxxsgKUL5gyV10cYRhds4pLJK93oUQB09nn9/rK2SQKBgCmLIbqxMtsBdDiVMaSgQCGZzvXiT27My6OKx5co+cGFdd2+jj6/sYknOGx5RiIyxRDbJ3KFVmzR/i1yAd5nLxW4ZdH+p+bGe5U97Rl0fRZ8lptLWs7WwreKDjAfuWVUrIBAFfJFJKxua2u4faZtvi9aEfffIdrMY+ModvRWBl1hAoGBAN2aTPLeKIhyKNg/8IIYcST7ixgCHLnT2fxmK7qnKd4Y0QtqMyQM+xwA5A89xUU8b8Q/+Z11UkM5ysh74dFR6R7Oha9Lw8V2ovx6vQLOATEK28X7FIBsolBPwMjAn572QN9eAP+uRe5Yu2KfiX6ez8D61rEFee+3AtnRHumDJokr"

const val RSA_PRIVATE = ""

private const val SDK_PAY_FLAG = 1
private const val SDK_AUTH_FLAG = 2

@SuppressLint("HandlerLeak")
private val mHandler: Handler = object : Handler() {
    @Suppress("unused")
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            SDK_PAY_FLAG -> {
                val payResult = PayResult(msg.obj as Map<String?, String?>)

                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                val resultInfo = payResult.result // 同步返回需要验证的信息
                val resultStatus = payResult.resultStatus
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        ToastUtils.showShort("支付成功");
                    Toast.makeText(BaseApp.context, "支付成功", Toast.LENGTH_SHORT).show()
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        ToastUtils.showShort("支付失败" + payResult);
                    Toast.makeText(BaseApp.context, "支付成功", Toast.LENGTH_SHORT).show()
                }
            }

            else -> {}
        }
    }
}

/**
 * 支付宝支付业务示例
 */
fun pay(activity: Activity) {
    if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(
            RSA_PRIVATE
        ))
    ) {
        return
    }

    /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */
    val rsa2 = (RSA2_PRIVATE.length > 0)
    val params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2)
    val orderParam = OrderInfoUtil2_0.buildOrderParam(params)

    val privateKey = if (rsa2) RSA2_PRIVATE else RSA_PRIVATE
    val sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2)
    val orderInfo = "$orderParam&$sign"

    val payRunnable = Runnable {
        val alipay = PayTask(activity)
        val result = alipay.payV2(orderInfo, true)
        Log.i("msp", result.toString())

        val msg: Message = Message()
        msg.what = SDK_PAY_FLAG
        msg.obj = result
        mHandler.sendMessage(msg)
    }

    // 必须异步调用
    val payThread = Thread(payRunnable)
    payThread.start()
}


@Composable
fun PayPage(modifier: Modifier = Modifier,navHostController: NavHostController) {


//    六位随机数
    val random = Random()
    val randomNum = random.nextInt(900000) + 100000
    val context = LocalContext.current
    var money by remember {
        mutableStateOf(100)
    }
    Column (modifier = modifier.fillMaxSize().padding(10.dp)) {
        OutlinedTextField(value = money.toString(), onValueChange = {
            money = it.toInt()
        }, label = {
            Text(text = "金额")
        })

        Button(onClick = {
            //开启沙箱
            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
            pay(context as Activity);
            navHostController.popBackStack()
        }, modifier = modifier.fillMaxWidth()) {
            Text(text = "支付")
        }
    }
}