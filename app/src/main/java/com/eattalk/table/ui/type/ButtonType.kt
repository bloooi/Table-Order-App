package com.eattalk.table.ui.type

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.theme.body14Medium
import com.eattalk.table.ui.theme.body16Medium

interface ButtonType {
    val default: ButtonStateType
    val pressed: ButtonStateType
    val disable: ButtonStateType
    val loading: ButtonStateType
}


@Keep
data class ButtonStateType(
    val solidColor: Color,
    val outlineColor: Color?,
    val labelColor: Color?,
    val iconColor: Color,
)

@Keep
sealed class ButtonSize(
    val height: Dp,
    val labelFont: TextStyle,
    val minPadding: Dp,
    val cornerRadius: Dp,
)

class Btn {
    data object Small : ButtonSize(
        height = 37.dp,
        labelFont = body14Medium,
        minPadding = 8.dp,
        cornerRadius = 8.dp
    )

    data object Medium : ButtonSize(
        height = 48.dp,
        labelFont = body16Medium,
        minPadding = 12.dp,
        cornerRadius = 8.dp
    )

    data object Large : ButtonSize(
        height = 56.dp,
        labelFont = body16Medium,
        minPadding = 16.dp,
        cornerRadius = 12.dp
    )

    class Solid {
        object Primary : ButtonType {
            override val default: ButtonStateType = ButtonStateType(
                solidColor = Ref.Primary.c300,
                outlineColor = null,
                labelColor = Ref.Neutral.c100,
                iconColor = Ref.Neutral.c100,
            )
            override val pressed: ButtonStateType = ButtonStateType(
                solidColor = Ref.Primary.c400,
                outlineColor = null,
                labelColor = Ref.Neutral.c100,
                iconColor = Ref.Neutral.c100,
            )
            override val disable: ButtonStateType = ButtonStateType(
                solidColor = Ref.Neutral.c200.copy(alpha = 0.3f),
                outlineColor = null,
                labelColor = Ref.Neutral.c200,
                iconColor = Ref.Neutral.c200,
            )
            override val loading: ButtonStateType = ButtonStateType(
                solidColor = Ref.Primary.c300,
                outlineColor = null,
                labelColor = Ref.Neutral.c100,
                iconColor = Ref.Neutral.c100,
            )

        }

        object Neutral : ButtonType {
            override val default: ButtonStateType = ButtonStateType(
                solidColor = Ref.Neutral.c100,
                outlineColor = Ref.Neutral.c200,
                labelColor = Ref.Neutral.c1000,
                iconColor = Ref.Neutral.c1000,
            )
            override val pressed: ButtonStateType = ButtonStateType(
                solidColor = Ref.Neutral.c200.copy(alpha = 0.3f),
                outlineColor = Ref.Neutral.c200,
                labelColor = Ref.Neutral.c1000,
                iconColor = Ref.Neutral.c1000,
            )
            override val disable: ButtonStateType = ButtonStateType(
                solidColor = Ref.Neutral.c200.copy(alpha = 0.3f),
                outlineColor = Ref.Stroke.c100,
                labelColor = Ref.Neutral.c200,
                iconColor = Ref.Neutral.c200,
            )
            override val loading: ButtonStateType = ButtonStateType(
                solidColor = Ref.Neutral.c100,
                outlineColor = Ref.Neutral.c200,
                labelColor = Ref.Neutral.c1000,
                iconColor = Ref.Neutral.c1000,
            )
        }

        object Error : ButtonType {
            override val default: ButtonStateType = ButtonStateType(
                solidColor = Ref.Error.c300,
                outlineColor = null,
                labelColor = Ref.Neutral.c100,
                iconColor = Ref.Neutral.c100,
            )
            override val pressed: ButtonStateType = ButtonStateType(
                solidColor = Ref.Error.c400,
                outlineColor = null,
                labelColor = Ref.Neutral.c100,
                iconColor = Ref.Neutral.c100,
            )
            override val disable: ButtonStateType = ButtonStateType(
                solidColor = Ref.Neutral.c200.copy(alpha = 0.3f),
                outlineColor = null,
                labelColor = Ref.Neutral.c200,
                iconColor = Ref.Neutral.c200,
            )

            override val loading: ButtonStateType = ButtonStateType(
                solidColor = Ref.Error.c300,
                outlineColor = null,
                labelColor = Ref.Neutral.c100,
                iconColor = Ref.Neutral.c100,
            )
        }
    }

    class Outline {
        object Primary : ButtonType {
            override val default: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = Ref.Primary.c300,
                labelColor = Ref.Primary.c300,
                iconColor = Ref.Primary.c300,
            )
            override val pressed: ButtonStateType = ButtonStateType(
                solidColor = Ref.Primary.a10,
                outlineColor = null,
                labelColor = Ref.Primary.c300,
                iconColor = Ref.Primary.c300,
            )
            override val disable: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = Ref.Neutral.c200.copy(alpha = 0.3f),
                labelColor = Ref.Neutral.c200,
                iconColor = Ref.Neutral.c200,
            )
            override val loading : ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = Ref.Primary.c300,
                labelColor = Ref.Primary.c300,
                iconColor = Ref.Primary.c300,
            )
        }

        object Neutral : ButtonType {
            override val default: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = Ref.Neutral.c200,
                labelColor = Ref.Neutral.c1000,
                iconColor = Ref.Neutral.c1000,
            )
            override val pressed: ButtonStateType = ButtonStateType(
                solidColor = Ref.Neutral.c200.copy(alpha = 0.3f),
                outlineColor = null,
                labelColor = Ref.Neutral.c1000,
                iconColor = Ref.Neutral.c1000,
            )
            override val disable: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = Ref.Stroke.c100,
                labelColor = Ref.Neutral.c200,
                iconColor = Ref.Neutral.c200,
            )
            override val loading: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = Ref.Neutral.c200,
                labelColor = Ref.Neutral.c1000,
                iconColor = Ref.Neutral.c1000,
            )

        }

        object Error : ButtonType {
            override val default: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = Ref.Error.c300,
                labelColor = Ref.Error.c300,
                iconColor = Ref.Error.c300,
            )
            override val pressed: ButtonStateType = ButtonStateType(
                solidColor = Ref.Error.a10,
                outlineColor = Ref.Error.a10,
                labelColor = Ref.Error.c300,
                iconColor = Ref.Error.c300,
            )
            override val disable: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = Ref.Stroke.c100,
                labelColor = Ref.Neutral.c200,
                iconColor = Ref.Neutral.c200,
            )
            override val loading: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = Ref.Error.c300,
                labelColor = Ref.Error.c300,
                iconColor = Ref.Error.c300,
            )
        }
    }

    class Ghost {
        object Primary : ButtonType {
            override val default: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = null,
                labelColor = Ref.Primary.c300,
                iconColor = Ref.Primary.c300,
            )
            override val pressed: ButtonStateType = ButtonStateType(
                solidColor = Ref.Primary.a10,
                outlineColor = null,
                labelColor = Ref.Primary.c300,
                iconColor = Ref.Primary.c300,
            )
            override val disable: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = null,
                labelColor = Ref.Neutral.c200,
                iconColor = Ref.Neutral.c200,
            )
            override val loading: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = null,
                labelColor = Ref.Primary.c300,
                iconColor = Ref.Primary.c300,
            )
        }

        object Neutral : ButtonType {
            override val default: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = null,
                labelColor = Ref.Neutral.c1000,
                iconColor = Ref.Neutral.c1000,
            )
            override val pressed: ButtonStateType = ButtonStateType(
                solidColor = Ref.Neutral.c200.copy(alpha = 0.3f),
                outlineColor = null,
                labelColor = Ref.Neutral.c1000,
                iconColor = Ref.Neutral.c1000,
            )
            override val disable: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = null,
                labelColor = Ref.Neutral.c200,
                iconColor = Ref.Neutral.c200,
            )
            override val loading: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = null,
                labelColor = Ref.Neutral.c1000,
                iconColor = Ref.Neutral.c1000,
            )
        }

        object Error : ButtonType {
            override val default: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = null,
                labelColor = Ref.Error.c300,
                iconColor = Ref.Error.c300,
            )
            override val pressed: ButtonStateType = ButtonStateType(
                solidColor = Ref.Error.a10,
                outlineColor = Ref.Error.a10,
                labelColor = Ref.Error.c300,
                iconColor = Ref.Error.c300,
            )
            override val disable: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = null,
                labelColor = Ref.Neutral.c200,
                iconColor = Ref.Neutral.c200,
            )
            override val loading: ButtonStateType = ButtonStateType(
                solidColor = Color.Transparent,
                outlineColor = null,
                labelColor = Ref.Error.c300,
                iconColor = Ref.Error.c300,
            )
        }
    }
}

class ButtonTypeProvider : PreviewParameterProvider<Pair<ButtonSize, ButtonType>> {
    override val values: Sequence<Pair<ButtonSize, ButtonType>>
        get() = sequenceOf(
            Pair(Btn.Large, Btn.Solid.Primary),
            Pair(Btn.Large, Btn.Outline.Primary),
            Pair(Btn.Large, Btn.Ghost.Primary),
            Pair(Btn.Medium, Btn.Solid.Primary),
            Pair(Btn.Medium, Btn.Outline.Primary),
            Pair(Btn.Medium, Btn.Ghost.Primary),
            Pair(Btn.Small, Btn.Solid.Primary),
            Pair(Btn.Small, Btn.Outline.Primary),
            Pair(Btn.Small, Btn.Ghost.Primary),

            Pair(Btn.Large, Btn.Solid.Neutral),
            Pair(Btn.Large, Btn.Outline.Neutral),
            Pair(Btn.Large, Btn.Ghost.Neutral),
            Pair(Btn.Medium, Btn.Solid.Neutral),
            Pair(Btn.Medium, Btn.Outline.Neutral),
            Pair(Btn.Medium, Btn.Ghost.Neutral),
            Pair(Btn.Small, Btn.Solid.Neutral),
            Pair(Btn.Small, Btn.Outline.Neutral),
            Pair(Btn.Small, Btn.Ghost.Neutral),

            Pair(Btn.Large, Btn.Solid.Error),
            Pair(Btn.Large, Btn.Outline.Error),
            Pair(Btn.Large, Btn.Ghost.Error),
            Pair(Btn.Medium, Btn.Solid.Error),
            Pair(Btn.Medium, Btn.Outline.Error),
            Pair(Btn.Medium, Btn.Ghost.Error),
            Pair(Btn.Small, Btn.Solid.Error),
            Pair(Btn.Small, Btn.Outline.Error),
            Pair(Btn.Small, Btn.Ghost.Error),
        )

}