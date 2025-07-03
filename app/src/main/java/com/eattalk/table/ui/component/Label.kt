package com.eattalk.table.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.type.Label
import com.eattalk.table.ui.type.LabelSize
import com.eattalk.table.ui.type.LabelStyle
import com.eattalk.table.ui.type.PillStyleProvider
import com.eattalk.table.ui.type.LabelType

@Composable
fun Label(
    modifier: Modifier = Modifier,
    style: LabelStyle,
    size: LabelSize,
    type: LabelType,
    icon: Pair<Painter, () -> Unit>? = null,
    text: String
) {
    val density = LocalDensity.current
    val shape = if (style == LabelStyle.Pill)
        RoundedCornerShape(100)
    else
        RoundedCornerShape(6.dp)


    val horizontalPadding = if (style == LabelStyle.Pill)
        8.dp
    else if (size == Label.Size.Large)
        12.dp
    else
        8.dp

    Box(
        modifier = modifier
            .height(with(density) { size.height.toDp() })
            .background(
                color = type.containerColor,
                shape = shape
            )
            .border(
                border = BorderStroke(1.dp, type.outlineColor),
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        val paddingModifier = if (icon == null) Modifier.padding(horizontal =horizontalPadding) else Modifier.padding(start = horizontalPadding, end = horizontalPadding -4.dp)

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier =paddingModifier
        ){
            StyleText(
                text = text,
                color = type.labelColor,
                style = size.labelStyle,

            )

            icon?.apply {
                IconButton(
                    onClick = icon.second,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(with(density) { size.labelStyle.textStyle.fontSize.toDp() })
                ) {
                    Icon(
                        painter = icon.first,
                        tint = type.labelColor,
                        contentDescription = "icon button"
                    )
                }
            }
        }
    }

}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun PreviewPill(@PreviewParameter(PillStyleProvider::class) items: List<Pair<LabelSize, LabelType>>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LabelStyle.entries.forEach { style ->
            Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
                items.forEach { data ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Label(
                            style = style,
                            size = data.first,
                            type = data.second,
                            text = "Label",
                            icon = painterResource(R.drawable.close) to {}
                        )

                        Label(
                            style = style,
                            size = data.first,
                            type = data.second,
                            text = "Label",
                        )
                    }
                }

            }
        }
    }
}