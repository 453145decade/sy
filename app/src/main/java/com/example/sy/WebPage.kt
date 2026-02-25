package com.example.sy

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

lateinit var webView: WebView
@Composable
fun WebPage() {
    var isShowP by remember{
        mutableStateOf(true)
    }
    var currP by remember{
        mutableStateOf(0f)
    }

    Column(modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = {
                webView.reload()
            }) {
                Text(text = "刷新")
            }
            Button(onClick = {
                webView.goForward()
            }) {
                Text(text = "前进")
            }
            Button(onClick = {
                webView.goBack()
            }) {
                Text(text = "后退")
            }
            Button(onClick = {
                webView.pauseTimers()
                webView.onPause()
            }) {
                Text(text = "暂停")
            }

        }


        if (isShowP) {
            LinearProgressIndicator( progress = currP)
        }

        AndroidView( factory = {
            webView = WebView(it)
            webView.loadUrl("https://www.baidu.com")
            webView.webViewClient = WebViewClient()
            val value = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    currP = newProgress.toFloat() / 100
                    if (newProgress == 100) {
                        isShowP = false
                    }
                }
            }
            webView.webChromeClient = value

            webView
        }, modifier = Modifier.fillMaxSize(), onRelease = {
            webView.destroy()
        })

    }



}