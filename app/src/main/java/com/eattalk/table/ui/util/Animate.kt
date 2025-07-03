package com.eattalk.table.ui.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color

@Composable
fun animateNullableColorAsState(
    targetValue: Color?,
    defaultColor: Color = Color.Unspecified,
    animationSpec: AnimationSpec<Color> = spring<Color>()
): State<Color?> {
    // 내부 non-null 애니메이션
    val animated = animateColorAsState(
        targetValue = targetValue ?: defaultColor,
        animationSpec = animationSpec
    )
    // derivedState로 다시 Nullable 매핑
    return derivedStateOf {
        if (targetValue == null) null else animated.value
    }
}