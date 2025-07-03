package com.eattalk.table.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.eattalk.table.presentation.start.SplashViewModel
import com.eattalk.table.ui.theme.AppTheme
import com.eattalk.table.ui.theme.Ref

@Composable
fun SplashScreen(viewModel: SplashViewModel = hiltViewModel()) {
    AppTheme(viewModel.dialogManager) {
        Box(
            modifier = Modifier
                .background(Ref.Background.c100)
                .fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Ref.Primary.c300
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSplashScreen() {
    SplashScreen()
}