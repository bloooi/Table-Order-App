package com.eattalk.table.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.theme.Ref

@Composable
fun DropDownItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    placeholder: String,
    text: String,
) {
    val shape = RoundedCornerShape(8.dp)
    Row(
        modifier = modifier
            .clip(shape)
            .border(BorderStroke(1.dp, Ref.Stroke.c100), shape)
            .background(if (enabled) Ref.Background.c200 else Ref.Neutral.c300)
            .clickable(onClick = onClick, enabled = enabled)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StyleText(
            text = text.ifEmpty { placeholder },
            color =if (text.isEmpty()) Ref.Neutral.c500 else if (enabled) Ref.Neutral.c1000 else Ref.Neutral.c600,
            style = Ref.Body.s16Medium
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(painter = painterResource(id = R.drawable.arrow_down), contentDescription = "Dropdown")
    }
}

@Preview
@Composable
private fun PreviewDropDownItem() {
    DropDownItem(
        placeholder = "language",
        text = "43434",
        enabled = false,
        onClick = {}
//        modifier = Modifier.width(200.dp)
    )
}