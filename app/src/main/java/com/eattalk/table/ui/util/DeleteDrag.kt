package com.eattalk.table.ui.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.component.StyleText
import com.eattalk.table.ui.theme.Ref
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeToDeleteItem(
    modifier: Modifier = Modifier,
    deleteThreshold: Dp = 150.dp,
    text: String = stringResource(R.string.delete),
    icon: Painter = painterResource(R.drawable.delete),
    onSelect: (() -> Unit)? = null,
    onDelete: () -> Unit,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    // 애니메이션/스코프
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val thresholdPx = with(LocalDensity.current) { deleteThreshold.toPx() }

    // 실제 아이템 크기를 저장할 상태 (bg 레이어를 같은 크기로 만드려고)
    var itemWidthPx by remember { mutableStateOf(0) }
    var itemHeightPx by remember { mutableStateOf(0) }

    // 둥근 모서리
    val shape = RoundedCornerShape(8.dp)

    // 항목 삭제 중인지 여부
    var isDeleting by remember { mutableStateOf(false) }

    // 항목 삭제 로직
    LaunchedEffect(isDeleting) {
        if (isDeleting) {
            // 왼쪽으로 완전히 스와이프 애니메이션
            offsetX.animateTo(
                targetValue = -2000f, // 화면 밖으로
                animationSpec = tween(durationMillis = 300)
            )

            // 애니메이션 후 실제 삭제
            onDelete()

            // 상태 초기화
            offsetX.snapTo(0f)
            isDeleting = false
        }
    }

    Box(modifier = modifier) {
        // 1) 스와이프해서 왼쪽으로 당겼을 때 배경(삭제 레이블) 보여주기
        if (offsetX.value < 0f) {
            Box(
                modifier = Modifier
                    .size(
                        width = with(LocalDensity.current) { itemWidthPx.toDp() },
                        height = with(LocalDensity.current) { itemHeightPx.toDp() }
                    )
                    .background(color = Ref.Error.c300, shape = shape)
                    .padding(end = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StyleText(
                        style = Ref.Body.s14Medium,
                        text = text,
                        color = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

        // 2) 실제 컨텐츠 레이어
        Box(
            Modifier
                .clip(shape)
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    enabled = enabled,
                    state = rememberDraggableState { delta ->
                        // 드래그 중엔 snapTo 로 바로바로 반영
                        if (!isDeleting) {
                            scope.launch {
                                // 왼쪽으로만 스와이프 허용 (delta가 음수일 때)
                                if (delta < 0 || offsetX.value < 0) {
                                    offsetX.snapTo((offsetX.value + delta).coerceAtLeast(-thresholdPx * 1.5f))
                                } else if (delta > 0) {
                                    offsetX.snapTo(0f)
                                }
                            }
                        }
                    },
                    onDragStopped = {
                        if (offsetX.value < -thresholdPx) {
                            // 임계값을 넘으면 삭제 프로세스 시작
                            isDeleting = true
                        } else {
                            // 그렇지 않으면 원래 위치로 복원
                            scope.launch {
                                offsetX.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(durationMillis = 200)
                                )
                            }
                        }
                    }
                )
                .clickable(onClick = { onSelect?.invoke() })
                // 측정을 위해 onGloballyPositioned
                .onGloballyPositioned { coords ->
                    itemWidthPx = coords.size.width
                    itemHeightPx = coords.size.height
                }
        ) {
            content()
        }
    }
}
