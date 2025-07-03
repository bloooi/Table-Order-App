package com.eattalk.table.ui.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.component.StyleText
import com.eattalk.table.ui.state.StoreItemUiState
import com.eattalk.table.ui.theme.Ref

@Composable
fun StoreItem(
    modifier: Modifier = Modifier,
    onClick: (StoreItemUiState) -> Unit,
    uiState: StoreItemUiState,
) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .clip(shape)
            .clickable(onClick = { onClick(uiState) })
            .background(Ref.Neutral.c100)
            .border(BorderStroke(1.dp, Ref.Stroke.c100), shape)
            .widthIn(min = 300.dp)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.product),
                contentDescription = "Store",
                tint = Ref.Neutral.c1000,
                modifier = Modifier.padding(end = 8.dp)
            )

            StyleText(
                text = uiState.name,
                color = Ref.Neutral.c1000,
                style = Ref.Body.s16Regular
            )
        }

        Column {
            StyleText(
                text = uiState.address1,
                color = Ref.Neutral.c800,
                style = Ref.Body.s14Regular
            )

            StyleText(
                text = uiState.address2,
                color = Ref.Neutral.c800,
                style = Ref.Body.s14Regular
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(widthDp = 700)
@Composable
private fun PreviewStoreItem() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        StoreItem(
            modifier = Modifier.weight(1f),
            onClick = {},
            uiState = StoreItemUiState(
                id = "",
                name = "Uptown Branch",
                address1 = "456 Park Avenue",
                address2 = "New York, NY 10022"
            )
        )

        StoreItem(
            modifier = Modifier.weight(1f),
            onClick = {},
            uiState = StoreItemUiState(
                id = "",
                name = "Downtown Branch",
                address1 = "456 Park Avenue",
                address2 = "New York, NY 10022"
            )
        )
    }
}