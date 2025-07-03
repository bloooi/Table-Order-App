package com.eattalk.table.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eattalk.table.ui.theme.AppTheme
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.theme.body14Regular
import com.eattalk.table.ui.util.DialogManager

@Composable
fun RadioButtonGroup(
    options: List<String>,
    modifier: Modifier = Modifier,
    onSelect: (Int) -> Unit,
    selectedIndex: Int,
) {
    Row(modifier = modifier) {
        options.forEachIndexed { index, option ->
            Row(
                modifier = Modifier
                    .padding(end = 24.dp)
                    .selectable(
                        selected = (index == selectedIndex),
                        onClick = { onSelect(index) },
                        role = Role.RadioButton,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedCanvasRadioButton(
                    selected = index == selectedIndex,
                    onClick = { onSelect(index) },
                    size = 14.dp,
                )
                Text(
                    text = option,
                    style = body14Regular,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}


@Composable
fun AnimatedCanvasRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 20.dp,
) {
    // 애니메이션 상태 정의
    val interactionSource = remember { MutableInteractionSource() }
    val borderWidth = with(LocalDensity.current) { (size.toPx() * 0.1f).toDp() }
    val duration = 100

    // 내부 원 크기 애니메이션
    val animatedInnerRadius by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(durationMillis = duration * 2),
        label = "inner Radius Animation"
    )

    val animatedColor by animateColorAsState(
        targetValue = if (selected) Ref.Primary.c300 else Ref.Neutral.c900,
        animationSpec = tween(durationMillis = duration),
        label = "Color Animation"
    )

    val dotRadius =
        animateDpAsState(
            targetValue = if (selected) (size * 0.9f) / 2 else 0.dp,
            animationSpec = tween(durationMillis = duration),
            label = "dot Radius Animation"
        )
    Box(
        modifier = modifier
            .size(size)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            // 외부 원
            drawCircle(
                color = if (enabled)
                    animatedColor
                else
                    Ref.Neutral.c900.copy(alpha = 0.38f),
                style = Stroke(
                    width = borderWidth.toPx(),
                    cap = StrokeCap.Round
                ),
                radius = (size / 2).toPx() - borderWidth.toPx() / 2
            )

            if (animatedInnerRadius > 0) {
                drawCircle(
                    color = if (enabled)
                        animatedColor
                    else
                        Ref.Neutral.c900.copy(alpha = 0.38f),
                    radius = dotRadius.value.toPx() - borderWidth.toPx() * 2
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun PreviewRadioButtonGroup() {
    val options = listOf("Option 1", "Option 2", "Option 3")
    var selectedOption by remember { mutableIntStateOf(0) }
    AppTheme(DialogManager()) {
        Column {
            RadioButtonGroup(
                options = options,
                onSelect = {
                    selectedOption = it
                },
                selectedIndex = selectedOption,
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = "Selected Option : ${if (selectedOption < 0) "None" else options[selectedOption]}")
        }
    }


}