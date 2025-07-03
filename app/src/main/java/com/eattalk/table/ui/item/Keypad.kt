package com.eattalk.table.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.component.StyleText
import com.eattalk.table.ui.theme.Ref

@Composable
fun Keypad(
    onInput: (String) -> Unit,
    functionText: String ="C",
    onFunction: () -> Unit,
    onDelete: () -> Unit,
    verticalPadding:Dp = 16.dp,
    background: Color = Ref.Background.c100,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
            .background(background)
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 352.dp)
                .padding(vertical = 24.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // First row: 1, 2, 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KeypadButton(text = "1", onClick = { onInput("1") }, verticalPadding = verticalPadding, modifier = Modifier.weight(1f))
                KeypadButton(text = "2", onClick = { onInput("2") }, verticalPadding = verticalPadding, modifier = Modifier.weight(1f))
                KeypadButton(text = "3", onClick = { onInput("3") }, verticalPadding = verticalPadding, modifier = Modifier.weight(1f))
            }

            // Second row: 4, 5, 6
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KeypadButton(text = "4", onClick = { onInput("4") }, verticalPadding = verticalPadding, modifier = Modifier.weight(1f))
                KeypadButton(text = "5", onClick = { onInput("5") }, verticalPadding = verticalPadding, modifier = Modifier.weight(1f))
                KeypadButton(text = "6", onClick = { onInput("6") }, verticalPadding = verticalPadding, modifier = Modifier.weight(1f))
            }

            // Third row: 7, 8, 9
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KeypadButton(text = "7", onClick = { onInput("7") }, verticalPadding = verticalPadding, modifier = Modifier.weight(1f))
                KeypadButton(text = "8", onClick = { onInput("8") }, verticalPadding = verticalPadding, modifier = Modifier.weight(1f))
                KeypadButton(text = "9", onClick = { onInput("9") }, verticalPadding = verticalPadding, modifier = Modifier.weight(1f))
            }

            // Fourth row: C, 0, Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KeypadButton(text = functionText, onClick = { onFunction() }, verticalPadding = verticalPadding, modifier = Modifier.weight(1f))
                KeypadButton(text = "0", onClick = { onInput("0") }, verticalPadding = verticalPadding, modifier = Modifier.weight(1f))
                KeypadButton(
                    content = {
                        StyleText(
                            text = "",
                            color = Ref.Neutral.c1000,
                            style = Ref.Head.s20,
                            modifier = Modifier.padding(vertical =verticalPadding)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.keypad_delete),
                            contentDescription = "Delete",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    },
                    onClick = { onDelete() },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun KeypadButton(
    verticalPadding:Dp = 16.dp,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    KeypadButton(
        content = {
            StyleText(
                text = text,
                color = Ref.Neutral.c1000,
                style = Ref.Head.s20,
                modifier = Modifier.padding(vertical = verticalPadding)
            )
        },
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun KeypadButton(
    content: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Ref.Neutral.a10, RoundedCornerShape(8.dp))
            .clickable (onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),

        ) {
        }
        content()
    }
}

@Preview(widthDp = 610)
@Composable
private fun PreviewKeypad() {
    var text by remember {
        mutableStateOf("")
    }
    Column {
        StyleText(text = "text : $text, number : ${text.toIntOrNull() ?: ""}", color = Ref.Neutral.c1000, style = Ref.Body.s16Medium)
        Keypad(
            onInput = {
                text += it
            },
            onFunction = {
                text = ""
            },
            onDelete = {
                if (text.isEmpty()) return@Keypad
                text = text.take(text.length - 1)
            }
        )
    }
}