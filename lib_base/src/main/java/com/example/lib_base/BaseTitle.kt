package com.example.lib_base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun BaseTitle(title:String,navHostController: NavHostController,name:String  ="") {
    Column(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.ArrowBack,"", modifier = Modifier.clickable {
                navHostController.popBackStack()
            })
            Text(title, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
        Spacer(Modifier.height(5.dp))
        Divider()
    }
}