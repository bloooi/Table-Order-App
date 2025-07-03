package com.eattalk.table.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eattalk.table.ui.theme.Ref

@Composable
fun LoadingLayout(
    isLoading: Boolean,
) {
    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(animationSpec = tween(durationMillis = 200)),
        exit  = fadeOut(animationSpec = tween(durationMillis = 200))
    ) {
        LoadingScreen()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .background(Ref.Neutral.a50)
            .fillMaxSize()
    ){
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = Ref.Primary.c200
        )
    }
}

@Composable
fun LoadingPopupScreen() {
    Box(
        modifier = Modifier
            .background(Ref.Neutral.c100)
            .fillMaxSize()
    ){
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = Ref.Primary.c300
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240", showBackground = true)
@Composable
private fun PreviewLoadingScreen() {
    var loading by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Button(onClick = { loading = !loading }) {

        }
        LoadingLayout(loading)
    }
}

@Preview
@Composable
private fun PreviewLoadingPopupScreen() {
    LoadingPopupScreen()
}