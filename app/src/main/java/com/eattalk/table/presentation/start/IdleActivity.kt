
package com.eattalk.table.presentation.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import com.eattalk.table.MainActivity // 나중에 사용할 주문 화면 Activity
import com.eattalk.table.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IdleActivity : ComponentActivity() {

    private val viewModel: IdleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(manager = viewModel.dialogManager) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IdleScreen(
                        onNavigateToOrder = {
                            // 주문 화면(MainActivity)으로 이동
                            startActivity(Intent(this, MainActivity::class.java))
                            finish() // IdleActivity는 종료
                        }
                    )
                }
            }
        }
    }
}
