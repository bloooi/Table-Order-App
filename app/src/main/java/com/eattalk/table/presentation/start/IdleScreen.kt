
package com.eattalk.table.presentation.start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eattalk.table.model.Language

@Composable
fun IdleScreen(
    viewModel: IdleViewModel = hiltViewModel(),
    onNavigateToOrder: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { onNavigateToOrder() },
        contentAlignment = Alignment.Center
    ) {
        // TODO: 매장 로고 또는 프로모션 이미지 표시
        Text(
            text = "화면을 터치하여 시작하세요",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        LanguageSelectionUI(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            onLanguageSelect = {
                viewModel.onLanguageSelected(it)
                onNavigateToOrder() // 언어 선택 시 바로 주문 화면으로 이동
            }
        )
    }
}

@Composable
fun LanguageSelectionUI(
    modifier: Modifier = Modifier,
    supportedLanguages: List<Language> = listOf(Language.KOREAN, Language.ENGLISH, Language.JAPANESE),
    onLanguageSelect: (Language) -> Unit
) {
    if (supportedLanguages.size > 1) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Please select your language",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            supportedLanguages.forEach { lang ->
                Button(onClick = { onLanguageSelect(lang) }) {
                    Text(text = lang.displayName)
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun IdleScreenPreview() {
    IdleScreen(onNavigateToOrder = {})
}
