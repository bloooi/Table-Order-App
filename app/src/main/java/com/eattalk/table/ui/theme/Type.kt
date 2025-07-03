package com.eattalk.table.ui.theme

import androidx.annotation.Keep
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.eattalk.table.R

val pretendard = FontFamily(
    Font(R.font.pretendard_regular, weight = FontWeight.Normal),
    Font(R.font.pretendard_medium, weight = FontWeight.Medium),
    Font(R.font.pretendard_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.pretendard_bold, weight = FontWeight.Bold),
    Font(R.font.pretendard_extra_bold, weight = FontWeight.ExtraBold),
)

@Keep
data class AppTextStyle(
    val textStyle: TextStyle,
    val topBaselinePadding: TextUnit,
    val bottomBaselinePadding: TextUnit
)

val label10 = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 10.sp,
    letterSpacing = 0.sp
)

val label12 = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    letterSpacing = 0.sp
)

val label14 = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = 0.sp
)

val body12Regular = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.sp
)

val body12Medium = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.sp
)

val body14Regular = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 21.sp,
    letterSpacing = 0.sp
)

val body14Medium = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 21.sp,
    letterSpacing = 0.sp
)

val body16Regular = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
)

val body16Medium = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp
)


val head20 = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    lineHeight = 30.sp,
    letterSpacing = 0.sp
)

val head24 = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.SemiBold,
    fontSize = 24.sp,
    lineHeight = 36.sp,
    letterSpacing = 0.sp
)

val head28 = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 42.sp,
    letterSpacing = 0.sp
)

val head34 = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Bold,
    fontSize = 34.sp,
    lineHeight = 51.sp,
    letterSpacing = 0.sp
)

val head40 = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 40.sp,
    lineHeight = 60.sp,
    letterSpacing = 0.sp
)

val head48 = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 48.sp,
    lineHeight = 72.sp,
    letterSpacing = 0.sp
)


val Typography = Typography(
    displayLarge = head48,
    displayMedium = head40,
    displaySmall = head34,
    headlineLarge = head28,
    headlineMedium = head24,
    headlineSmall = head20,
    titleLarge = body16Medium,
    titleMedium = body14Medium,
    titleSmall = body12Medium,
    bodyLarge = body16Regular,
    bodyMedium = body14Regular,
    bodySmall = body12Regular,
    labelLarge = label14,
    labelMedium = label12,
    labelSmall = label10
)