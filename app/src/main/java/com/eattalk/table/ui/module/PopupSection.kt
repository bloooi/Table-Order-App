package com.eattalk.table.ui.module

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.eattalk.table.ui.component.StyleText
import com.eattalk.table.ui.theme.Ref

@Composable
fun PopupSection(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StyleText(
            text = title,
            color = Ref.Neutral.c900,
            style = Ref.Body.s14Medium
        )

        content()
    }
}

@Composable
fun PopupSection(
    modifier: Modifier = Modifier,
    title: AnnotatedString,
    content: @Composable () -> Unit
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StyleText(
            text = title,
            color = Ref.Neutral.c900,
            style = Ref.Body.s14Medium
        )

        content()
    }
}