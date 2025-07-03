package com.eattalk.table.ui.type

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.eattalk.table.ui.theme.AppTextStyle
import com.eattalk.table.ui.theme.Ref

enum class LabelStyle {
    Pill, Badge
}


interface LabelColor {
    val primary: LabelType
    val secondary: LabelType
    val white: LabelType
    val gray: LabelType
    val red: LabelType
    val green: LabelType
    val yellow: LabelType
    val reverse: LabelType?
}

sealed class LabelSize(
    val height: TextUnit,
    val labelStyle: AppTextStyle,
)

data class LabelType(
    val labelColor: Color,
    val outlineColor: Color,
    val containerColor: Color
)

class Label {
    class Size {
        data object Small : LabelSize(
            height = 20.sp,
            labelStyle = Ref.Label.s12,
        )

        data object Medium : LabelSize(
            height = 21.sp,
            labelStyle = Ref.Label.s14,
        )

        data object Large : LabelSize(
            height = 25.sp,
            labelStyle = Ref.Label.s14
        )
    }

    object Solid : LabelColor {
        override val primary: LabelType = LabelType(
            labelColor = Ref.Primary.c300,
            outlineColor = Ref.Primary.c300,
            containerColor = Ref.Primary.a10
        )

        override val secondary: LabelType = LabelType(
            labelColor = Ref.Secondary.c400,
            outlineColor = Ref.Secondary.c400,
            containerColor = Ref.Secondary.a10
        )
        override val white: LabelType = LabelType(
            labelColor = Ref.Neutral.c500,
            outlineColor = Ref.Stroke.c100,
            containerColor = Ref.Neutral.a10
        )
        override val gray: LabelType = LabelType(
            labelColor = Ref.Neutral.c500,
            outlineColor = Ref.Neutral.c500,
            containerColor = Ref.Neutral.c100
        )
        override val red: LabelType = LabelType(
            labelColor = Ref.Error.c300,
            outlineColor = Ref.Error.c300,
            containerColor = Ref.Error.a10
        )
        override val green: LabelType = LabelType(
            labelColor = Ref.Accent.c400,
            outlineColor = Ref.Accent.c400,
            containerColor = Ref.Accent.a10
        )
        override val yellow: LabelType = LabelType(
            labelColor = Ref.Warning.c400,
            outlineColor = Ref.Warning.c400,
            containerColor = Ref.Warning.a10
        )
        override val reverse: LabelType = LabelType(
            labelColor = Ref.Neutral.c100,
            outlineColor = Ref.Neutral.c500,
            containerColor = Ref.Neutral.c700
        )
    }

    object Outline : LabelColor {
        override val primary: LabelType = LabelType(
            labelColor = Ref.Primary.c300,
            outlineColor = Ref.Primary.c300,
            containerColor = Color.Transparent
        )

        override val secondary: LabelType = LabelType(
            labelColor = Ref.Secondary.c300,
            outlineColor = Ref.Secondary.c300,
            containerColor = Color.Transparent
        )

        override val white: LabelType = LabelType(
            labelColor = Ref.Neutral.c500,
            outlineColor = Ref.Stroke.c100,
            containerColor = Color.Transparent
        )
        override val gray: LabelType = LabelType(
            labelColor = Ref.Neutral.c500,
            outlineColor = Ref.Neutral.c500,
            containerColor = Color.Transparent
        )
        override val red: LabelType = LabelType(
            labelColor = Ref.Error.c300,
            outlineColor = Ref.Error.c300,
            containerColor = Color.Transparent
        )
        override val green: LabelType = LabelType(
            labelColor = Ref.Accent.c400,
            outlineColor = Ref.Accent.c400,
            containerColor = Color.Transparent
        )
        override val yellow: LabelType = LabelType(
            labelColor = Ref.Warning.c400,
            outlineColor = Ref.Warning.c400,
            containerColor = Color.Transparent
        )
        override val reverse: LabelType? = null
    }
    object Ghost {
        val neutral: LabelType = LabelType (
            labelColor = Ref.Neutral.c1000,
            outlineColor = Color.Transparent,
            containerColor = Color.Transparent
        )
    }
}

class PillStyleProvider : PreviewParameterProvider<List<Pair<LabelSize, LabelType>>> {
    override val values: Sequence<List<Pair<LabelSize, LabelType>>>
        get() = sequenceOf(
            listOf(
                Pair(Label.Size.Small, Label.Solid.primary),
                Pair(Label.Size.Medium, Label.Solid.primary),
                Pair(Label.Size.Large, Label.Solid.primary),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Solid.secondary),
                Pair(Label.Size.Medium, Label.Solid.secondary),
                Pair(Label.Size.Large, Label.Solid.secondary),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Solid.white),
                Pair(Label.Size.Medium, Label.Solid.white),
                Pair(Label.Size.Large, Label.Solid.white),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Solid.gray),
                Pair(Label.Size.Medium, Label.Solid.gray),
                Pair(Label.Size.Large, Label.Solid.gray),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Solid.red),
                Pair(Label.Size.Medium, Label.Solid.red),
                Pair(Label.Size.Large, Label.Solid.red),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Solid.green),
                Pair(Label.Size.Medium, Label.Solid.green),
                Pair(Label.Size.Large, Label.Solid.green),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Solid.yellow),
                Pair(Label.Size.Medium, Label.Solid.yellow),
                Pair(Label.Size.Large, Label.Solid.yellow),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Solid.reverse),
                Pair(Label.Size.Medium, Label.Solid.reverse),
                Pair(Label.Size.Large, Label.Solid.reverse),
            ),

            listOf(
                Pair(Label.Size.Small, Label.Outline.primary),
                Pair(Label.Size.Medium, Label.Outline.primary),
                Pair(Label.Size.Large, Label.Outline.primary),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Outline.secondary),
                Pair(Label.Size.Medium, Label.Outline.secondary),
                Pair(Label.Size.Large, Label.Outline.secondary),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Outline.white),
                Pair(Label.Size.Medium, Label.Outline.white),
                Pair(Label.Size.Large, Label.Outline.white),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Outline.gray),
                Pair(Label.Size.Medium, Label.Outline.gray),
                Pair(Label.Size.Large, Label.Outline.gray),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Outline.red),
                Pair(Label.Size.Medium, Label.Outline.red),
                Pair(Label.Size.Large, Label.Outline.red),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Outline.green),
                Pair(Label.Size.Medium, Label.Outline.green),
                Pair(Label.Size.Large, Label.Outline.green),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Outline.yellow),
                Pair(Label.Size.Medium, Label.Outline.yellow),
                Pair(Label.Size.Large, Label.Outline.yellow),
            ),
            listOf(
                Pair(Label.Size.Small, Label.Ghost.neutral),
                Pair(Label.Size.Medium, Label.Ghost.neutral),
                Pair(Label.Size.Large, Label.Ghost.neutral),
            )
        )
}
