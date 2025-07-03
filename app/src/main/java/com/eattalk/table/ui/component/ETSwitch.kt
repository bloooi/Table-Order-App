package com.eattalk.table.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eattalk.table.ui.theme.AppTheme
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.util.DialogManager

@Composable
fun ETSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    // 색상 정의
    val trackColorOn = Ref.Primary.c300 // 파란색
    val trackColorOff = Ref.Neutral.c500 // 회색
    val thumbColor = Color.White

    // 트랙 및 썸 크기
    val trackWidth = 46.dp
    val trackHeight = 30.dp
    val thumbSize = 24.dp
    val thumbPadding = 2.dp

    // 애니메이션 값 계산
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - thumbPadding else thumbPadding,
        label = "thumbOffset"
    )

    val trackAlpha by animateFloatAsState(
        targetValue = if (checked) 1f else 1f,
        label = "trackAlpha"
    )

//    val currentTrackColor = if (checked) trackColorOn else trackColorOff
    val currentTrackColor by animateColorAsState(
        targetValue = if (checked) trackColorOn else trackColorOff,
        label = "trackColor"
    )

    // 트랙과 썸을 포함한 컨테이너
    Box(
        modifier = modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(CircleShape)
            .background(currentTrackColor.copy(alpha = trackAlpha))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled
            ) {
                onCheckedChange(!checked)
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // 썸(동그라미)
        Box(
            modifier = Modifier
                .size(thumbSize)
                .offset(x = thumbOffset)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomSwitchPreview() {
    var checked by remember { mutableStateOf(false) }

    AppTheme(manager = DialogManager()) {
        Surface {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                ETSwitch(
                    checked = true,
                    onCheckedChange = { }
                )

                Spacer(modifier = Modifier.width(24.dp))

                ETSwitch(
                    checked = false,
                    onCheckedChange = { }
                )

                Spacer(modifier = Modifier.width(24.dp))

                ETSwitch(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
        }
    }
}