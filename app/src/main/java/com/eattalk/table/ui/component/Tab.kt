package com.eattalk.table.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.theme.AppTheme
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.theme.body14Medium
import com.eattalk.table.ui.type.Btn
import com.eattalk.table.ui.util.DialogManager

// 탭 스타일 정의를 위한 Enum 클래스
enum class TabStyle {
    UNDERLINED,  // 하단 라인이 있는 기본 탭 스타일
    ROUNDED      // 둥근 모서리와 배경색이 있는 카테고리 스타일
}

@Composable
fun CustomizableTabLayout(
    tabs: List<Pair<String, String>>,
    selectedTabIndex: Int,
    onTabSelected: (Int, String) -> Unit,
    tabStyle: TabStyle = TabStyle.UNDERLINED,
) {
    when (tabStyle) {
        TabStyle.UNDERLINED -> ScrollableUnderlinedTabLayout(
            tabs = tabs,
            editMode = true,
            endPadding = 100.dp,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = onTabSelected,
            extraFunction = painterResource(R.drawable.add) to {}
        )

        TabStyle.ROUNDED -> RoundedTabLayout(
            tabs = tabs.map { it.second },
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { onTabSelected(it, tabs[it].first) },
        )
    }
}

@Composable
fun RoundedTabLayout(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 스크롤 가능한 탭 영역
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    RoundedTab(
                        title = title,
                        selected = selectedTabIndex == index,
                        onClick = { onTabSelected(index) }
                    )
                }
            }
        }
    }
}

@Composable
fun RoundedTab(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) {
        Ref.Primary.a10
    } else {
        Ref.Neutral.c100
    }

    val textColor = if (selected) {
        Ref.Primary.c300
    } else {
        Ref.Neutral.c700
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50)) // 완전히 둥근 모서리
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        StyleText(
            text = title,
            color = textColor,
            style = Ref.Body.s14Medium
        )
    }
}

@Composable
fun UnderlinedTabLayout(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TabRow 영역
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.weight(1f),
            containerColor = Ref.Background.c200,
            indicator = { tabPositions ->
                // 선택된 탭 밑에만 인디케이터 표시
                if (selectedTabIndex < tabPositions.size) {
                    AnimatedTextWidthIndicator(
                        selectedTabIndex = selectedTabIndex,
                        tabPositions = tabPositions,
                        tabTitles = tabs
                    )
                }
            },
            divider = {
                // 전체 탭 밑에 구분선 표시
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Ref.Neutral.c200
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            text = title,
                            style = body14Medium,
                            color = if (selectedTabIndex == index) Ref.Primary.c300 else Ref.Neutral.c500
                        )
                    }
                )
            }
        }
    }
}

// 스크롤 가능한 언더라인 탭 버전
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableUnderlinedTabLayout(
    editMode: Boolean = false,
    tabs: List<Pair<String, String>>, // id, text
    endPadding: Dp = 0.dp,
    selectedTabIndex: Int,
    extraFunction: Pair<Painter, () -> Unit>? = null,
    onEdit: ((Int, String, String) -> Unit)? = null, // index, id, text
    onTabSelected: (Int, String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 스크롤 가능한 TabRow 영역
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .weight(1f)
                .padding(end = endPadding),
            containerColor = Color.Transparent,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                AnimatedTextWidthIndicator(
                    selectedTabIndex = selectedTabIndex,
                    tabPositions = tabPositions,
                    tabTitles = tabs.map { it.second },
                    isEditMode = editMode
                )
            },
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Ref.Neutral.c200
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index, tab.first) },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StyleText(
                                text = tab.second,
                                style = Ref.Body.s14Medium,
                                color = if (selectedTabIndex == index) Ref.Primary.c300 else Ref.Neutral.c500
                            )

                            if (selectedTabIndex == index && editMode) {
                                IconButton(
                                    onClick = { onEdit?.invoke(index, tab.first, tab.second) },
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .size(20.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.edit),
                                        contentDescription = "Edit",
                                        tint = Ref.Primary.c300,
                                    )
                                }
                            }
                        }
                    }
                )
            }

            extraFunction?.let {
                IconButton(
                    onClick = extraFunction.second,
                    modifier = Modifier
                        .size(Btn.Small.height)
                ) {
                    Icon(
                        painter = extraFunction.first,
                        tint = Ref.Neutral.c500,
                        contentDescription = "${extraFunction.first}",
                    )
                }
            }

        }
    }
}

// 커스텀 텍스트 너비 인디케이터 (애니메이션 포함)
@Composable
fun AnimatedTextWidthIndicator(
    selectedTabIndex: Int,
    tabPositions: List<TabPosition>,
    tabTitles: List<String>,
    isEditMode: Boolean = false,
    color: Color = MaterialTheme.colorScheme.primary,
    height: Dp = 2.dp
) {
    // 각 탭 제목의 텍스트 너비를 미리 측정
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
    val density = LocalDensity.current

    // 각 탭의 텍스트 너비 계산
    val textWidths = remember(tabTitles) {
        tabTitles.map { title ->
            val textSize = textMeasurer.measure(text = title, style = textStyle)
            with(density) { textSize.size.width.toDp() + if (isEditMode) 28.dp else 0.dp }
        }
    }

    // 현재 선택된 탭 위치
    val currentTabPosition = tabPositions[selectedTabIndex]
    val currentTextWidth = textWidths[selectedTabIndex]

    // 탭의 중앙 위치 계산
    val tabCenter =
        currentTabPosition.left + (currentTabPosition.right - currentTabPosition.left) / 2

    val indicatorStart by animateDpAsState(
        targetValue = tabCenter - currentTextWidth / 2,
        // 튕김 없는 직선 애니메이션
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "indicatorStartAnimation"
    )

    // 애니메이션 적용된 너비
    val indicatorWidth by animateDpAsState(
        targetValue = currentTextWidth,
        animationSpec = tween(
            durationMillis = 200,
            easing = LinearOutSlowInEasing
        ),
        label = "indicatorWidthAnimation"
    )

    // 인디케이터 그리기
    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = indicatorStart)
            .width(indicatorWidth)
            .height(height)
            .background(color = color)
    )
}

// 예시 화면
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabStyleSelectorScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var currentTabStyle by remember { mutableStateOf(TabStyle.UNDERLINED) }

    val tabs = listOf(
        "tab1" to "All Items",
        "tab2" to "Main Dishes",
        "tab3" to "Beverages",
        "tab4" to "Desserts"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tab Style Selector") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 스타일 선택 옵션
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { currentTabStyle = TabStyle.ROUNDED },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentTabStyle == TabStyle.ROUNDED)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text("둥근 탭 스타일")
                }

                Button(
                    onClick = { currentTabStyle = TabStyle.UNDERLINED },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentTabStyle == TabStyle.UNDERLINED)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text("밑줄 탭 스타일")
                }
            }

            // 선택된 스타일에 따른 탭 레이아웃
            CustomizableTabLayout(
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index, id ->
                    selectedTabIndex = index
                },
                tabStyle = currentTabStyle,
            )

            // 선택된 탭 콘텐츠
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${tabs[selectedTabIndex]} 콘텐츠\n현재 스타일: ${currentTabStyle.name}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1280)
@Composable
fun RoundedTabStylePreview() {
    MaterialTheme {
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf(
            "tab1" to "All Items",
            "tab2" to "Main Dishes",
            "tab3" to "Beverages",
            "tab4" to "Desserts"
        )

        CustomizableTabLayout(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { index, _ ->
                selectedTabIndex = index
            },
            tabStyle = TabStyle.ROUNDED,
        )
    }
}

@Preview(showBackground = true, widthDp = 1280)
@Composable
fun UnderlinedTabStylePreview() {
    MaterialTheme {
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf(
            "tab1" to "All Items",
            "tab2" to "Main Dishes",
            "tab3" to "Beverages",
            "tab4" to "Desserts",
            "tab5" to "All Items",
            "tab6" to "Main Dishes",
            "tab7" to "Beverages",
            "tab8" to "Desserts",
            "tab9" to "All Items",
            "tab10" to "Main Dishes",
            "tab11" to "Beverages",
            "tab12" to "Desserts",
            "tab13" to "All Items",
            "tab14" to "Main Dishes",
            "tab15" to "Beverages",
            "tab16" to "Desserts",
            "tab17" to "All Items",
            "tab18" to "Main Dishes",
            "tab19" to "Beverages",
            "tab20" to "Desserts",
            "tab21" to "All Items",
            "tab22" to "Main Dishes",
            "tab23" to "Beverages",
            "tab24" to "Desserts",
        )

        CustomizableTabLayout(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { index, _ ->
                selectedTabIndex = index
            },
            tabStyle = TabStyle.UNDERLINED,
        )
    }
}

@Preview(showBackground = true, widthDp = 1280)
@Composable
fun TabStyleSelectorScreenPreview() {
    AppTheme(DialogManager()) {
        TabStyleSelectorScreen()
    }
}
