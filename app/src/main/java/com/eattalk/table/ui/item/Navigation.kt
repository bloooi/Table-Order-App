package com.eattalk.table.ui.item

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.component.StyleText
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.type.Navigation

@Composable
fun NavigationItem(
    icon: Painter,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val selectedIconColor by animateColorAsState(
        targetValue = if (selected) Ref.Primary.c300 else Ref.Neutral.c200,
        label = "animated icon color"
    )

    val selectedTextColor by animateColorAsState(
        targetValue = if (selected) Ref.Primary.c100 else Ref.Neutral.c100,
        label = "animated text color"
    )
    val selectedSurfaceColor by animateColorAsState(
        targetValue = if (selected) Ref.Primary.a10 else Color.Transparent,
        label = "animated surface color"
    )

    Box(
        modifier = Modifier
            .clickable(onClick = onClick, interactionSource = null, indication = null)
            .padding(vertical = 8.dp)
    ){
        Column(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = selectedSurfaceColor)

            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = "$label navigation icon",
                tint = selectedIconColor,
                modifier = Modifier
                    .size(24.dp)
                    .padding(bottom = 4.dp)
            )

            StyleText(
                text = label,
                color = selectedTextColor,
                style = Ref.Body.s12Medium
            )

        }
    }
}

@Composable
fun NavigationBar(
    onSelected: (Navigation) -> Unit,
    edgeContent: @Composable () -> Unit,
) {
    var selected by remember { mutableStateOf(Navigation.all.first()) }
    NavigationRail(
        containerColor = Ref.Neutral.c1000
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Navigation.all.forEach {
            NavigationItem(
                icon = painterResource(id = it.icon),
                label = stringResource(id = it.label),
                selected = selected == it,
                onClick = {
                    selected = it
                    onSelected(it)
                },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        edgeContent()
    }
}

@Composable
fun NavigationBar(
    selectedRoute: String?,
    onSelected: (Navigation) -> Unit,
    edgeContent: @Composable () -> Unit,
) {
    NavigationRail(
        containerColor = Ref.Neutral.c1000
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Navigation.all.forEach {
            NavigationItem(
                icon = painterResource(id = it.icon),
                label = stringResource(id = it.label),
                selected = selectedRoute == it.route,
                onClick = {
                    onSelected(it)
                },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        edgeContent()
    }
}

@Composable
fun SingleNavigationBar(
    onBack: () -> Unit,
    text: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxHeight()
            .background(Ref.Neutral.c1000)
            .padding(8.dp)
            .padding(top = 8.dp)
    ) {
        NavigationItem(
            icon = painterResource(R.drawable.back),
            label = text,
            selected = false,
            onClick = onBack
        )
    }

    
}

@Preview(showBackground = true, backgroundColor = 0xFF333333)
@Composable
private fun PreviewNavigationItem() {
    val items = listOf(
        Pair(painterResource(id = R.drawable.product), "Product"),
        Pair(painterResource(id = R.drawable.grid), "Tables"),
    )

    var selectedIndex by remember { mutableStateOf(0) }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items.forEachIndexed { index, pair ->
            NavigationItem(
                icon = pair.first,
                label = pair.second,
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNavigationBar() {
    Scaffold { innerPadding ->
        Row(Modifier.padding(innerPadding)) {
            NavigationBar(
                null,
                {},
                {
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSingleavigation() {
    SingleNavigationBar(
        onBack = {},
        text = "Payment"
    )
}