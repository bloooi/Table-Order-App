package com.eattalk.table.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.eattalk.table.ui.util.DialogManager
import com.eattalk.table.ui.util.DialogScreen


val LightColorScheme = lightColorScheme(
    primary = Ref.Primary.c300,                    // 주 색상: 파란색 (0xFF4A90E2)
    onPrimary = Ref.Neutral.c100,                  // 주 색상 대비: 흰색
    primaryContainer = Ref.Primary.c100,           // 주 색상 컨테이너: 연한 파란색 (0xFFC3DAF5)
    onPrimaryContainer = Ref.Primary.c600,         // 주 색상 컨테이너 대비: 진한 파란색 (0xFF134075)
    inversePrimary = Ref.Primary.a50,              // 반전 주 색상: primary50 (반투명)
    secondary = Ref.Secondary.c300,                // 보조 색상: (0xFF83ced7)
    onSecondary = Ref.Neutral.c100,                // 보조 색상 대비: 흰색
    secondaryContainer = Ref.Secondary.c100,       // 보조 색상 컨테이너: 연한 색상 (0xFFdaf0f3)
    onSecondaryContainer = Ref.Secondary.c600,     // 보조 색상 컨테이너 대비: (0xFF256b75)
    tertiary = Ref.Accent.c300,                    // 강조 색상: (0xFF30d158)
    onTertiary = Ref.Neutral.c100,                 // 강조 색상 대비: 흰색
    tertiaryContainer = Ref.Accent.c100,           // 강조 색상 컨테이너: 연한 초록 (0xFFbaf0c7)
    onTertiaryContainer = Ref.Accent.c600,         // 강조 색상 컨테이너 대비: (0xFF156128)
    background = Ref.Background.c100,              // 배경: (0xFFF9FAFB)
    onBackground = Ref.Neutral.c900,               // 배경 대비: 어두운 회색 (0xFF4a4a4a)
    surface = Ref.Neutral.c100,                    // 표면: 흰색
    onSurface = Ref.Neutral.c900,                  // 표면 대비: 어두운 회색
    surfaceVariant = Ref.Neutral.c300,             // 표면 변형: 연한 회색 (0xFFd2d2d2)
    onSurfaceVariant = Ref.Neutral.c700,           // 표면 변형 대비: 중간 회색 (0xFF777777)
    surfaceTint = Ref.Primary.c300,                // 표면 틴트: 주 색상
    inverseSurface = Ref.Neutral.c900,             // 반전 표면: 어두운 회색
    inverseOnSurface = Ref.Neutral.c100,           // 반전 표면 대비: 흰색
    error = Ref.Error.c300,                        // 오류 색상: (0xFFff3b30)
    onError = Ref.Neutral.c100,                    // 오류 대비: 흰색
    errorContainer = Ref.Error.c100,               // 오류 컨테이너: 연한 오류색 (0xFFffbeba)
    onErrorContainer = Ref.Error.c600,             // 오류 컨테이너 대비: (0xFF890700)
    outline = Ref.Stroke.c100,                     // 윤곽선: (0xFFE9EAEB)
    outlineVariant = Ref.Neutral.c400,             // 윤곽선 변형: (0xFFbbbbbb)
    scrim = Ref.Neutral.a0,                        // 스크림: 투명
    surfaceBright = Ref.Background.c200,           // 밝은 표면: (0xFFFDFEFF)
    surfaceContainer = Ref.Neutral.c100,           // 컨테이너 표면: 흰색
    surfaceContainerHigh = Ref.Neutral.c200,       // 컨테이너 높은: (0xFFe8e8e8)
    surfaceContainerHighest = Ref.Neutral.c300,    // 컨테이너 가장 높은: (0xFFd2d2d2)
    surfaceContainerLow = Ref.Neutral.c400,        // 컨테이너 낮은: (0xFFbbbbbb)
    surfaceContainerLowest = Ref.Neutral.c500,     // 컨테이너 가장 낮은: (0xFFa4a4a4)
    surfaceDim = Ref.Neutral.c600                    // 어두운 표면: (0xFF8e8e8e)
)

@Composable
fun AppTheme(
    manager: DialogManager,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ){
        DialogScreen(manager = manager) {
            content()
        }
    }
}