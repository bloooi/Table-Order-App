package com.eattalk.table.presentation.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.eattalk.table.ui.screen.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    val viewModel: SplashViewModel by viewModels<SplashViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashScreen()
        }

        lifecycleScope.launch {
            viewModel.goToStart.collect {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finishAffinity()
            }
        }

        lifecycleScope.launch {
            viewModel.goToMain.collect {
//                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finishAffinity()
            }
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                finishAffinity()
            }
        }

//        viewModel.tempInsertData()
        viewModel.checkSession()

    }

}