package com.eattalk.table.ui.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.eattalk.table.ui.theme.Ref

@Composable
fun ForceLightSystemBars() {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.navigationBarColor = Ref.Neutral.c1000.toArgb()
            window.statusBarColor = Ref.Neutral.c1000.toArgb()

            // ✅ 핵심: 아이콘/텍스트 색상을 밝게 강제
            // isAppearanceLight... 를 false로 설정하면 아이콘이 밝아집니다.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }
}