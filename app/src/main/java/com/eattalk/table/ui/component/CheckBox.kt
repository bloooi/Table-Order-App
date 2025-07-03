package com.eattalk.table.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eattalk.table.ui.theme.Ref

@Composable
fun CheckBox(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    enabled: Boolean = true,
    padding: Dp = 4.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CustomCheckbox(
            isChecked = checked,
            onCheckedChange = onCheckedChange,
            checkedColor = Ref.Primary.c300,
            uncheckedColor = Ref.Primary.a10,
            enabled = enabled,
        )

        Spacer(modifier = Modifier.width(padding))

        StyleText(
            text = text, color = Ref.Neutral.c900, style = Ref.Body.s16Regular,
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun PreviewCheckBox() {
    var checked by remember { mutableStateOf(false) }
    Surface(
        color = Ref.Background.c100
    ) {
        Column {
            CheckBox(
                text = "Text",
                checked = true,
                onCheckedChange = { }
            )
            CheckBox(text = "Text", checked = checked, onCheckedChange = { checked = it })

            CheckBox(
                text = "Text",
                checked = true,
                enabled = false,
                onCheckedChange = {}
            )


            CheckBox(
                text = "Text",
                checked = false,
                enabled = false,
                onCheckedChange = {}
            )
        }
    }

}


@Composable
fun CustomCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    cornerRadius: Dp = 4.dp, // 모서리 둥글기 조정
    enabled: Boolean,
    checkedColor: Color = Color.Blue,
    uncheckedColor: Color = Color.Gray
) {
    val checkboxColor by animateColorAsState(
        targetValue = if (isChecked)
            if (enabled) checkedColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        else
            if (enabled) uncheckedColor else Color.Transparent
    )

    val borderSize by animateDpAsState(
        targetValue = if (isChecked) 0.dp else 1.dp
    )

    Box(
        modifier = modifier
            .size(size)
            .background(color = checkboxColor, shape = RoundedCornerShape(cornerRadius))
            .border(
                borderSize,
                color = if (enabled) Ref.Stroke.c100 else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.38f
                ),
                RoundedCornerShape(cornerRadius)
            )
            .clickable(enabled = enabled) { onCheckedChange(!isChecked) },
        contentAlignment = Alignment.Center
    ) {
        if (isChecked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

