package com.eattalk.table.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp

class Ref {
    object Product {
        val Blue = Color(0xFFDBEAFE)
        val Yellow = Color(0xFFFEF3C7)
        val Gray = Color(0xFFF8F9FA)
        val Red = Color(0xFFFEE2E2)
        val Green = Color(0xFFDCFCE7)
        val Purple = Color(0xFFF3E8FF)

        val colors = listOf(Neutral.c100, Gray, Yellow, Red, Green, Purple)
    }


    object Primary {
        val c100 = Color(0xFFC3DAF5)
        val c200 = Color(0xFF86B5EC)
        val c300 = Color(0xFF4A90E2)
        val c400 = Color(0xFF2274D3)
        val c500 = Color(0xFF1A5AA4)
        val c600 = Color(0xFF134075)
        val a10 = c300.copy(alpha = 0.1f)
        val a50 = c300.copy(alpha = 0.5f)
    }

    object Secondary {
        val c100 = Color(0xFFdaf0f3)
        val c200 = Color(0xFFb4e1e7)
        val c300 = Color(0xFF83ced7)
        val c400 = Color(0xFF53bac8)
        val c500 = Color(0xFF3598a5)
        val c600 = Color(0xFF256b75)
        val a10 = c200.copy(alpha = 0.1f)
        val a50 = c200.copy(alpha = 0.5f)
    }

    object Accent {
        val c100 = Color(0xFFbaf0c7)
        val c200 = Color(0xFF75e090)
        val c300 = Color(0xFF30d158)
        val c400 = Color(0xFF26ac48)
        val c500 = Color(0xFF1e8738)
        val c600 = Color(0xFF156128)
        val a10 = c300.copy(alpha = 0.1f)
        val a50 = c300.copy(alpha = 0.5f)
    }

    object Neutral {
        val c100 = Color(0xFFffffff)
        val c200 = Color(0xFFe8e8e8)
        val c300 = Color(0xFFd2d2d2)
        val c400 = Color(0xFFbbbbbb)
        val c500 = Color(0xFFa4a4a4)
        val c600 = Color(0xFF8e8e8e)
        val c700 = Color(0xFF777777)
        val c800 = Color(0xFF606060)
        val c900 = Color(0xFF4a4a4a)
        val c1000 = Color(0xFF333333)
        val a10 = c1000.copy(alpha = 0.1f)
        val a50 = c1000.copy(alpha = 0.5f)
        val a0 = c100.copy(alpha = 0f)
    }

    object Background {
        val c100 = Color(0xFFF9FAFB)
        val c200 = Color(0xFFFDFEFF)
    }

    object Stroke {
        val c100 = Color(0xFFE9EAEB)
    }

    object Error {
        val c100 = Color(0xFFffbeba)
        val c200 = Color(0xFFff7c75)
        val c300 = Color(0xFFff3b30)
        val c400 = Color(0xFFf80d00)
        val c500 = Color(0xFFc00a00)
        val c600 = Color(0xFF890700)
        val a10 = c300.copy(alpha = 0.1f)
        val a50 = c300.copy(alpha = 0.5f)
    }

    object Warning {
        val c100 = Color(0xFFfff2bf)
        val c200 = Color(0xFFffe57f)
        val c300 = Color(0xFFffd940)
        val c400 = Color(0xFFffcc00)
        val c500 = Color(0xFFc69e00)
        val c600 = Color(0xFF8c7000)
        val a10 = c400.copy(alpha = 0.1f)
        val a50 = c400.copy(alpha = 0.5f)
    }

    object Success {
        val c100 = Color(0xFFccf2d5)
        val c200 = Color(0xFF98e4ab)
        val c300 = Color(0xFF65d782)
        val c400 = Color(0xFF34c759)
        val c500 = Color(0xFF289a45)
        val c600 = Color(0xFF1d6e31)
        val a10 = c400.copy(alpha = 0.1f)
        val a50 = c400.copy(alpha = 0.5f)
    }

    object Label {
        val s10 = AppTextStyle(
            textStyle = label10,
            topBaselinePadding = 10.sp,
            bottomBaselinePadding = 2.sp
        )

        val s12 = AppTextStyle(
            textStyle = label12,
            topBaselinePadding = 11.sp,
            bottomBaselinePadding = 3.sp
        )

        val s14 = AppTextStyle(
            textStyle = label14,
            topBaselinePadding = 13.sp,
            bottomBaselinePadding = 4.sp
        )
    }

    object Body {
        val s12Regular = AppTextStyle(
            textStyle = body12Regular,
            topBaselinePadding = 13.sp,
            bottomBaselinePadding = 5.sp
        )

        val s12Medium = AppTextStyle(
            textStyle = body12Medium,
            topBaselinePadding = 13.sp,
            bottomBaselinePadding = 5.sp
        )

        val s14Regular = AppTextStyle(
            textStyle = body14Regular,
            topBaselinePadding = 15.sp,
            bottomBaselinePadding = 6.sp
        )

        val s14Medium = AppTextStyle(
            textStyle = body14Medium,
            topBaselinePadding = 15.sp,
            bottomBaselinePadding = 6.sp
        )

        val s16Regular = AppTextStyle(
            textStyle = body16Regular,
            topBaselinePadding = 18.sp,
            bottomBaselinePadding = 6.sp
        )

        val s16Medium = AppTextStyle(
            textStyle = body16Medium,
            topBaselinePadding = 18.sp,
            bottomBaselinePadding = 6.sp
        )
    }

    object Head {
        val s20 = AppTextStyle(
            textStyle = head20,
            topBaselinePadding = 22.sp,
            bottomBaselinePadding = 8.sp
        )

        val s24 = AppTextStyle(
            textStyle = head24,
            topBaselinePadding = 27.sp,
            bottomBaselinePadding = 9.sp
        )

        val s28 = AppTextStyle(
            textStyle = head28,
            topBaselinePadding = 31.sp,
            bottomBaselinePadding = 11.sp
        )

        val s34 = AppTextStyle(
            textStyle = head34,
            topBaselinePadding = 38.sp,
            bottomBaselinePadding = 13.sp
        )

        val s40 = AppTextStyle(
            textStyle = head40,
            topBaselinePadding = 44.sp,
            bottomBaselinePadding = 16.sp
        )

        val s48 = AppTextStyle(
            textStyle = head48,
            topBaselinePadding = 53.sp,
            bottomBaselinePadding = 19.sp
        )
    }
}

class TypoProvider : PreviewParameterProvider<List<Pair<String, AppTextStyle>>> {
    override val values: Sequence<List<Pair<String, AppTextStyle>>>
        get() = sequenceOf(
            listOf(
                Pair("Label 10", Ref.Label.s10),
                Pair("Label 12", Ref.Label.s12),
                Pair("Label 14", Ref.Label.s14),
                Pair("Body 12 Regular", Ref.Body.s12Regular),
                Pair("Body 12 Medium", Ref.Body.s12Medium),
                Pair("Body 14 Regular", Ref.Body.s14Regular),
                Pair("Body 14 Medium", Ref.Body.s14Medium),
                Pair("Body 16 Regular", Ref.Body.s16Regular),
                Pair("Body 16 Medium", Ref.Body.s16Medium),
                Pair("Head 20", Ref.Head.s20),
                Pair("Head 24", Ref.Head.s24),
                Pair("Head 28", Ref.Head.s28),
                Pair("Head 34", Ref.Head.s34),
                Pair("Head 40", Ref.Head.s40),
                Pair("Head 48", Ref.Head.s48),
            )
        )
}

class ProductColorProvider: PreviewParameterProvider<List<Color>> {
    override val values: Sequence<List<Color>>
        get() = sequenceOf(
            listOf(
                Ref.Product.Blue,
                Ref.Product.Gray,
                Ref.Product.Red,
                Ref.Product.Purple,
                Ref.Product.Yellow,
                Ref.Product.Green,
            )
        )

}