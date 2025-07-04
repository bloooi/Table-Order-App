package com.eattalk.table

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.eattalk.table.presentation.start.IdleActivity
import com.eattalk.table.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(manager = viewModel.dialogManager) {
                // 타임아웃 이벤트 발생 시 IdleActivity로 이동
                LaunchedEffect(Unit) {
                    viewModel.timeoutEvent.collect {
                        val intent = Intent(this@MainActivity, IdleActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                        startActivity(intent)
                        finish()
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInteropFilter {
                            when (it.action) {
                                MotionEvent.ACTION_DOWN -> viewModel.resetIdleTimer()
                            }
                            false // 이벤트를 하위 컴포저블로 전달
                        }
                ) {
                    // TODO: 주문 화면 UI 구현 (TOS-100)
                }
            }
        }
    }
}