package com.eattalk.table.presentation.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.eattalk.table.api.websocket.WebSocketService
import com.eattalk.table.ui.screen.StartLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.jvm.java

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    val viewModel: LoginViewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StartLayout()
        }

        lifecycleScope.launch {
            viewModel.finishLogin.collect {
                val applicationContext = this@LoginActivity.applicationContext
                ContextCompat.startForegroundService(applicationContext, Intent(applicationContext, WebSocketService::class.java)) // 로그인 이후 재시도

//                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finishAffinity()
            }
        }
    }
}