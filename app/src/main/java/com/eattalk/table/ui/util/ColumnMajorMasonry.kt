package com.eattalk.table.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun StrictOrderStaggeredLayout(
    modifier: Modifier = Modifier,
    columns: Int = 4,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        if (measurables.isEmpty()) {
            return@Layout layout(constraints.minWidth, constraints.minHeight) {}
        }

        val horizontalSpacingPx = horizontalSpacing.roundToPx()
        val verticalSpacingPx = verticalSpacing.roundToPx()

        // 1. Calculate column width
        val totalHorizontalSpacing = horizontalSpacingPx * (columns - 1)
        val columnWidth = (constraints.maxWidth - totalHorizontalSpacing) / columns

        // Constraints for measuring each item
        val itemConstraints = constraints.copy(
            minWidth = columnWidth,
            maxWidth = columnWidth
        )

        // 2. Measure items and calculate positions
        val columnHeights = IntArray(columns) { 0 } // Track height of each column
        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(itemConstraints)

            // Determine column and position
            val columnIndex = index % columns
            val xPosition = columnIndex * (columnWidth + horizontalSpacingPx)
            val yPosition = columnHeights[columnIndex]

            // Update column height
            columnHeights[columnIndex] += placeable.height + verticalSpacingPx

            // Store placeable with its calculated position
            Triple(placeable, xPosition, yPosition)
        }

        // 3. Calculate total layout height (tallest column)
        // Subtract the last spacing added if columns have items
        // ❷ 수정: 부모 제약에 맞춰 클램프
        val rawHeight = (columnHeights.maxOrNull() ?: 0) - verticalSpacingPx
        val finalHeight = rawHeight
            .coerceAtLeast(constraints.minHeight)
            .coerceAtMost(constraints.maxHeight)

        layout(constraints.maxWidth, finalHeight) {
            placeables.forEach { (placeable, x, y) ->
                placeable.placeRelative(x, y)
            }
        }
    }
}

// --- 사용 예시 ---
@Composable
fun CustomLayoutExample() {
    val itemCount = 20
    val items = List(itemCount) { index ->
        // 랜덤 높이를 위한 데이터 (실제로는 아이템 내용에 따라 결정됨)
        (Random.nextInt(50, 200)).dp
    }

    Column(Modifier.fillMaxSize()) {
        StrictOrderStaggeredLayout(
            modifier = Modifier

                .fillMaxWidth(),
            columns = 4,
            horizontalSpacing = 8.dp,
            verticalSpacing = 8.dp
        ) {
            items.forEachIndexed { index, height ->
                Box(
                    modifier = Modifier
                        .height(height) // 아이템 높이 지정
                        .fillMaxWidth()
                        .background(Color(Random.nextLong(0xFFFFFFFF)).copy(alpha = 1f))
                        .border(1.dp, Color.Black)
                ) {
                    Text("Item ${index + 1}", Modifier.padding(4.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCustomLayoutExample() {
    CustomLayoutExample()
}