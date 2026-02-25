package com.example.sy.View

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController

@Composable
fun ViewPaint(modifier: Modifier = Modifier,navHostController: NavHostController) {

    Column {
        AndroidView(factory = {
//            val myView = MyView(it)
//            myView
            val myView = MyView(it)
//            myView.start(5,navHostController)
            myView
        })
    }

}



