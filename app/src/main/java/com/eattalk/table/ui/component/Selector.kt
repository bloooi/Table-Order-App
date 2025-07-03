package com.eattalk.table.ui.component

import android.icu.math.BigDecimal
import android.icu.util.Currency
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.type.Selector
import com.eattalk.table.ui.type.SelectorStyle
import com.eattalk.table.ui.type.SelectorStyleProvider
import com.eattalk.table.ui.util.animateNullableColorAsState
import com.eattalk.table.util.format

@Composable
fun Selector(
    modifier: Modifier = Modifier,
    style: SelectorStyle,
    title: String,
    subText: String? = null,
    icon: Painter? = null,
    selected: Boolean,
    enabled: Boolean = true,
    onSelected: (Boolean) -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)

    val backgroundColor by animateColorAsState(
        targetValue = if (!enabled) {
            Ref.Neutral.a10
        } else if (selected) {
            style.selectedContainerColor
        } else {
            style.containerColor
        }
    )


    val textColor by animateColorAsState(
        targetValue = if (!enabled) {
            Ref.Neutral.c500
        } else if (selected) {
            style.selectedContentColor
        } else {
            style.labelColor
        }
    )

    val iconColor by animateNullableColorAsState(
        targetValue = if (selected) {
            style.selectedContentColor
        } else {
            style.iconColor
        }
    )

    val borderColor by animateNullableColorAsState(
        targetValue = if (selected) {
            style.selectedBorderColor
        } else {
            style.borderColor
        }
    )

    val borderModifier =
        borderColor?.let { Modifier.border(BorderStroke(1.dp, it), shape = shape) } ?: Modifier

    Box(
        modifier = modifier
            .heightIn(min = style.height)
            .clip(shape)
            .background(backgroundColor, shape = shape)
            .then(borderModifier)
            .clickable(enabled = enabled) {
                onSelected(!selected)
            }
            .padding(
                horizontal = style.horizontalPadding,
                vertical = if (subText != null) 8.dp else 0.dp
            ),
        contentAlignment = if (style == Selector.menu) Alignment.CenterStart else Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null && iconColor != null) {
                    Icon(
                        painter = icon,
                        contentDescription = "Selector Icon",
                        tint = iconColor ?: Color.Transparent,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp)
                    )
                }

                StyleText(
                    text = title,
                    color = textColor,
                    style = Ref.Body.s14Medium
                )
            }

            subText?.let {
                StyleText(
                    text = it,
                    color = if (enabled) Ref.Neutral.c700 else Ref.Neutral.c500,
                    style = Ref.Body.s12Regular,

                    )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1500)
@Composable
private fun PreviewSelector(@PreviewParameter(SelectorStyleProvider::class) data: List<SelectorStyle>) {
    var selected by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        data.forEach {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Selector(
                        style = it,
                        title = "Item",
                        selected = selected
                    ) {
                        selected = it
                    }

                    Selector(
                        style = it,
                        title = "Item",
                        selected = !selected
                    ) {
                        selected = it
                    }

                    Selector(
                        style = it,
                        icon = painterResource(id = R.drawable.language),
                        title = "Item",
                        selected = selected
                    ) {
                        selected = it
                    }

                    Selector(
                        style = it,
                        icon = painterResource(id = R.drawable.time),
                        title = "Item",
                        selected = !selected
                    ) {
                        selected = it
                    }
                }
                Selector(
                    modifier = Modifier.fillMaxWidth(),
                    style = it,
                    title = "Item",
                    selected = selected
                ) {
                    selected = it
                }

                Selector(
                    modifier = Modifier.fillMaxWidth(),
                    style = it,
                    title = "Item",
                    selected = !selected
                ) {
                    selected = it
                }

                Selector(
                    modifier = Modifier.fillMaxWidth(),
                    style = it,
                    title = "Item",
                    subText = BigDecimal(-12.99).format(Currency.getInstance("EUR")),
                    selected = !selected,
                ) {
                    selected = it
                }

                Selector(
                    modifier = Modifier.fillMaxWidth(),
                    style = it,
                    title = "Item",
                    subText = BigDecimal(-12.99).format(Currency.getInstance("EUR")),
                    selected = !selected,
                    enabled = false
                ) {
                    selected = it
                }
            }
        }
    }
}