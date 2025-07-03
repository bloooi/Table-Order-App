import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eattalk.table.ui.component.StyleText
import com.eattalk.table.ui.theme.Ref


// Data class for segmented button options
data class SegmentedButtonOption(
    val text: String,
    val icon: Painter? = null
)

enum class SegmentedButtonWidthMode {
    WRAP_CONTENT,  // 버튼이 필요한 만큼만 너비를 차지
    EQUAL_WIDTH,   // 버튼이 공간을 동일하게 분배하지만 전체 너비는 콘텐츠에 의존
    FILL_SPACE     // 버튼이 사용 가능한 모든 공간을 채움 (기존 동작)
}

@Composable
fun SegmentedButton(
    options: List<SegmentedButtonOption>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    widthMode: SegmentedButtonWidthMode = SegmentedButtonWidthMode.WRAP_CONTENT
) {
    val localDensity = LocalDensity.current
    var itemWidths by remember { mutableStateOf(List(options.size) { 0.dp }) }
    var itemOffsets by remember { mutableStateOf(List(options.size) { 0.dp }) }

    // State to track whether animation should be applied (not on initial render)
    var isFirstRender by remember { mutableStateOf(true) }
    var previousIndex by remember { mutableStateOf(selectedIndex) }

    // Calculate the position for animation
    val selectionOffset = if (itemOffsets.isNotEmpty() && selectedIndex < itemOffsets.size) {
        itemOffsets[selectedIndex]
    } else {
        0.dp
    }

    val selectionWidth = if (itemWidths.isNotEmpty() && selectedIndex < itemWidths.size) {
        itemWidths[selectedIndex]
    } else {
        0.dp
    }

    // Check if index has changed from user interaction
    if (previousIndex != selectedIndex) {
        isFirstRender = false
        previousIndex = selectedIndex
    }

    // Animate the position based on whether it's first render or not
    val animatedOffset by animateDpAsState(
        targetValue = selectionOffset,
        animationSpec = if (isFirstRender) tween(0) else tween(300),
        label = "offset"
    )

    val animatedWidth by animateDpAsState(
        targetValue = selectionWidth,
        animationSpec = if (isFirstRender) tween(0) else tween(300),
        label = "width"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Ref.Neutral.c200)
            .padding(4.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    ) {
        // Animated selection background
        if (itemWidths.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .offset(x = animatedOffset)
                    .width(animatedWidth)
                    .height(40.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Ref.Background.c100)
            )
        }

        // Buttons Row
        Row(
            modifier = when (widthMode) {
                SegmentedButtonWidthMode.FILL_SPACE -> Modifier.fillMaxWidth()
                else -> Modifier.wrapContentWidth()
            },
            horizontalArrangement = when (widthMode) {
                SegmentedButtonWidthMode.EQUAL_WIDTH -> Arrangement.SpaceEvenly
                else -> Arrangement.Start
            }
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = selectedIndex == index

                Box(
                    modifier = Modifier
                        .let {
                            when (widthMode) {
                                SegmentedButtonWidthMode.FILL_SPACE -> it.weight(1f)
                                SegmentedButtonWidthMode.EQUAL_WIDTH -> it.weight(1f)
                                SegmentedButtonWidthMode.WRAP_CONTENT -> it
                            }
                        }
                        .onGloballyPositioned { coordinates ->
                            with(localDensity) {
                                // Update this item's width and offset in our lists
                                val newItemWidth = coordinates.size.width.toDp()
                                val newOffset = coordinates.positionInParent().x.toDp()

                                if (itemWidths.getOrNull(index) != newItemWidth ||
                                    itemOffsets.getOrNull(index) != newOffset
                                ) {

                                    val newWidths = itemWidths.toMutableList()
                                    val newOffsets = itemOffsets.toMutableList()

                                    if (index < newWidths.size) newWidths[index] = newItemWidth
                                    if (index < newOffsets.size) newOffsets[index] = newOffset

                                    itemWidths = newWidths
                                    itemOffsets = newOffsets
                                }
                            }
                        }
                        .clickable { onSelectionChanged(index) }
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = when (widthMode) {
                            SegmentedButtonWidthMode.WRAP_CONTENT -> Modifier
                                .wrapContentWidth()
                                .heightIn(20.dp)

                            else -> Modifier
                                .fillMaxWidth()
                                .heightIn(20.dp)
                        }
                    ) {
                        val iconTint by animateColorAsState(
                            targetValue = if (isSelected) Ref.Neutral.c1000 else Ref.Neutral.c600,
                            animationSpec = if (isFirstRender) tween(0) else tween(300),
                            label = "iconTint"
                        )

                        option.icon?.let {
                            Icon(
                                painter = option.icon,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        val textColor by animateColorAsState(
                            targetValue = if (isSelected) Ref.Neutral.c1000 else Ref.Neutral.c600,
                            animationSpec = if (isFirstRender) tween(0) else tween(300),
                            label = "textColor"
                        )

                        StyleText(
                            text = option.text,
                            style = Ref.Body.s14Medium,
                            color = textColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentedButtonDemo() {
    // State to track the selected index
    var selectedIndex by remember { mutableStateOf(0) }

    // Define options with text and icons
    val options = listOf(

        SegmentedButtonOption(
            text = "%",
            icon = null
        ),
        SegmentedButtonOption(
            text = "USD",
            icon = null
        )
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("기본 너비 모드 (WRAP_CONTENT):")
        Spacer(modifier = Modifier.height(8.dp))
        // Implement the segmented button with wrap content
        SegmentedButton(
            options = options,
            selectedIndex = selectedIndex,
            onSelectionChanged = { selectedIndex = it },
            modifier = Modifier,
            widthMode = SegmentedButtonWidthMode.WRAP_CONTENT
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("동일 너비 모드 (EQUAL_WIDTH):")
        Spacer(modifier = Modifier.height(8.dp))
        // Implement the segmented button with equal width
        SegmentedButton(
            options = options,
            selectedIndex = selectedIndex,
            onSelectionChanged = { selectedIndex = it },
            modifier = Modifier,
            widthMode = SegmentedButtonWidthMode.EQUAL_WIDTH
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("가득 채우기 모드 (FILL_SPACE):")
        Spacer(modifier = Modifier.height(8.dp))
        // Implement the segmented button with fill width
        SegmentedButton(
            options = options,
            selectedIndex = selectedIndex,
            onSelectionChanged = { selectedIndex = it },
            modifier = Modifier.fillMaxWidth(),
            widthMode = SegmentedButtonWidthMode.FILL_SPACE
        )
    }
}


@Preview(widthDp = 686)
@Composable
private fun PreviewSegmentedButton() {
    SegmentedButtonDemo()
}