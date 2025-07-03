package com.eattalk.table.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.component.NormalButton
import com.eattalk.table.ui.component.StyleText
import com.eattalk.table.ui.item.StoreItem
import com.eattalk.table.ui.state.StoreItemUiState
import com.eattalk.table.ui.theme.AppTheme
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.type.Btn
import com.eattalk.table.ui.util.DialogManager

@Composable
fun SelectStoreScreen(
    onLogout: () -> Unit,
    onStoreClick: (StoreItemUiState) -> Unit,
    stores: List<StoreItemUiState>?
) {
    Column(
        modifier = Modifier
            .background(Ref.Background.c200)
            .padding(horizontal = 32.dp, vertical = 24.dp)
            .fillMaxSize(),
    ) {
        StyleText(
            text = stringResource(R.string.title_select_store),
            style = Ref.Head.s24,
            color = Ref.Neutral.c1000
        )
        stores?.apply {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .padding(vertical = 36.dp)
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(stores) { item ->
                    StoreItem(
                        uiState = item,
                        onClick = onStoreClick
                    )
                }
            }
        }

        NormalButton(
            onClick = onLogout,
            size = Btn.Medium,
            type = Btn.Solid.Neutral,
            text = stringResource(R.string.logout),
            icon = painterResource(R.drawable.logout)
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewSelectStoreScreen() {
    AppTheme(DialogManager()) {
        SelectStoreScreen(
            onLogout = {},
            onStoreClick = {},
            stores = listOf(
                StoreItemUiState(
                    id = "",
                    name = "Uptown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Downtown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Uptown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Downtown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Uptown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Downtown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Uptown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Downtown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Uptown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Downtown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Uptown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Downtown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Uptown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Downtown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Uptown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Downtown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Uptown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Downtown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Uptown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),
                StoreItemUiState(
                    id = "",
                    name = "Downtown Branch",
                    address1 = "456 Park Avenue",
                    address2 = "New York, NY 10022"
                ),

                )
        )
    }
}