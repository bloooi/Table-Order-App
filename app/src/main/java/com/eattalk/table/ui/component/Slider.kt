package com.eattalk.table.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Slider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.eattalk.table.ui.theme.Ref

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ETSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    range: ClosedFloatingPointRange<Float>,
    step: Int,
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        valueRange = range,
        steps = step,
        modifier = modifier,
        enabled = enabled,
        thumb = {
            Canvas(
                modifier = Modifier
                    .size(24.dp)
                    .shadow(10.dp, CircleShape)
            ) {
                // 반지름은 영역의 절반, 색상 지정
                drawCircle(
                    color = Color.White,
                    radius = size.minDimension / 2f
                )
            }
        },
        track = {
            SliderDefaults.Track(
                colors = SliderDefaults.colors(
                    activeTrackColor = Ref.Primary.c300,
                    inactiveTrackColor = Ref.Neutral.a10,
                    activeTickColor = Ref.Neutral.c100,
                    inactiveTickColor = Ref.Neutral.a10,
                ),
                sliderState = it,
                modifier = Modifier.height(12.dp),
                thumbTrackGapSize = 0.dp
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewSlider() {
    var value by remember { mutableStateOf(2f) }
    ETSlider(
        value = value,
        onValueChange = { value = it },
        range = 2f..6f,
        step = 1,
    )
}