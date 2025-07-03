package com.eattalk.table.ui.type

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.theme.body14Medium
import com.eattalk.table.ui.theme.body16Medium

data class SelectorStyle(
    val labelStyle: TextStyle,
    val labelColor: Color,
    val iconColor: Color?,
    val containerColor: Color,
    val borderColor: Color?,
    val selectedContentColor: Color,
    val selectedContainerColor: Color,
    val selectedBorderColor: Color?,
    val height: Dp,
    val horizontalPadding: Dp,
)

object Selector {
    val menu: SelectorStyle = SelectorStyle(
        labelStyle = body16Medium,
        labelColor = Ref.Neutral.c700,
        iconColor = Ref.Neutral.c900,
        containerColor = Color.Transparent,
        borderColor = null,
        selectedContentColor = Ref.Primary.c300,
        selectedContainerColor = Ref.Primary.a10,
        selectedBorderColor = null,
        height = 48.dp,
        horizontalPadding = 16.dp
    )
    val option: SelectorStyle = SelectorStyle(
        labelStyle = body14Medium,
        labelColor = Ref.Neutral.c900,
        iconColor = Ref.Neutral.c700,
        containerColor = Color.Transparent,
        borderColor = Ref.Stroke.c100,
        selectedContentColor = Ref.Primary.c300,
        selectedContainerColor = Ref.Primary.a10,
        selectedBorderColor = Ref.Primary.c100,
        height = 38.dp,
        horizontalPadding = 12.dp
    )
    val tag: SelectorStyle = SelectorStyle(
        labelStyle = body14Medium,
        labelColor = Ref.Neutral.c600,
        iconColor = Ref.Neutral.c500,
        containerColor = Ref.Neutral.c100,
        borderColor = Ref.Stroke.c100,
        selectedContentColor = Ref.Primary.c300,
        selectedContainerColor = Ref.Primary.a10,
        selectedBorderColor = Ref.Primary.c300,
        height = 45.dp,
        horizontalPadding = 12.dp
    )
    val optionItem: SelectorStyle = SelectorStyle(
        labelStyle = body14Medium,
        labelColor = Ref.Neutral.c600,
        iconColor = Ref.Neutral.c500,
        containerColor = Ref.Neutral.c100,
        borderColor = Ref.Stroke.c100,
        selectedContentColor = Ref.Primary.c300,
        selectedContainerColor = Ref.Primary.a10,
        selectedBorderColor = null,
        height = 42.dp,
        horizontalPadding = 12.dp
    )

    val errorOptionItem: SelectorStyle = SelectorStyle(
        labelStyle = body14Medium,
        labelColor = Ref.Neutral.c600,
        iconColor = Ref.Neutral.c500,
        containerColor = Ref.Neutral.c100,
        borderColor = Ref.Stroke.c100,
        selectedContentColor = Ref.Error.c300,
        selectedContainerColor = Ref.Error.a10,
        selectedBorderColor = null,
        height = 42.dp,
        horizontalPadding = 12.dp
    )

}

class SelectorStyleProvider : PreviewParameterProvider<List<SelectorStyle>> {
    override val values: Sequence<List<SelectorStyle>>
        get() = sequenceOf(
            listOf(
                Selector.menu,
                Selector.option,
                Selector.tag,
                Selector.errorOptionItem
            )
        )
}