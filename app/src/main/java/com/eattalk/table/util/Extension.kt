package com.eattalk.table.util

import android.icu.math.BigDecimal
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.icu.util.ULocale
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eattalk.table.ui.state.MultiLanguageState
import kotlinx.datetime.LocalDateTime

fun BigDecimal.format(currency: Currency): String {
    // NumberFormat을 사용해 통화 예시 포맷 생성 (예: 1234.56 포맷)
    val formatter = NumberFormat.getCurrencyInstance(ULocale.getDefault())
    formatter.currency = currency
    // 정수인지 확인 (소수점 이하가 0인지)
    val isInteger = this.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0

    // 정수일 경우 소수점 이하를 표시하지 않음
    if (isInteger) {
        formatter.minimumFractionDigits = 0
    }
    return formatter.format(this)
}

fun BigDecimal.formatWithoutSymbol(currency: Currency): String {
    // 일반 숫자 포맷터 생성
    val formatter = NumberFormat.getInstance(ULocale.getDefault())

    // 통화의 소수점 자릿수 설정 가져와서 적용
    formatter.maximumFractionDigits = currency.defaultFractionDigits
    formatter.minimumFractionDigits = currency.defaultFractionDigits

    // 포맷팅 적용
    return formatter.format(this)
}

fun String.toBigDecimal(
    currency: Currency,
): BigDecimal {
    if (this.isBlank()) {
        return BigDecimal.ZERO
    }

    return try {
        // 통화 형식에 맞는 NumberFormat 생성
        val numberFormat = NumberFormat.getCurrencyInstance(ULocale.getDefault())
        numberFormat.currency = currency

        // 통화 문자열 파싱
        val number = numberFormat.parse(this)
        BigDecimal(number.toString())
    } catch (e: Exception) {
        BigDecimal.ZERO
    }
}

fun String.parseLocale(): ULocale = ULocale(this)

fun String.format(): Currency? = try {
    Currency.getInstance(this)
} catch (e: Exception) {
    null
}

fun Modifier.dashedBorder(
    brush: Brush,
    shape: Shape,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round
) = this.drawWithContent {
    val outline = shape.createOutline(size, layoutDirection, density = this)
    val dashedStroke = Stroke(
        cap = cap,
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx())
        )
    )

    // Draw the content
    drawContent()

// Draw the border
    drawOutline(
        outline = outline,
        style = dashedStroke,
        brush = brush
    )
}

fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round
) = dashedBorder(brush = SolidColor(color), shape, strokeWidth, dashLength, gapLength, cap)

/**
 * LocalDateTime 을 "HH:mm, MMMM d, yyyy" 형태의 문자열로 포맷팅한다.
 * 예: 2025-03-14T22:00 → "22:00, March 14, 2025"
 */
fun LocalDateTime.toDisplayString(): String {
    // 시각 부분: "HH:mm"
    val hourStr   = hour.toString().padStart(2, '0')
    val minuteStr = minute.toString().padStart(2, '0')

    // 월 이름: Month enum 의 name 은 "JANUARY" 처럼 대문자 → "January" 로 변환
    val monthName = month.name
        .lowercase()                          // "january"
        .replaceFirstChar { it.titlecase() } // "January"

    // 일(dayOfMonth) 과 연도(year)
    val day  = dayOfMonth
    val year = year

    return "$hourStr:$minuteStr, $monthName $day, $year"
}

fun Map<String, String>.defaultLanguageText(mainLangCode: String): String =
    this[mainLangCode] ?: this.entries.first().value

fun Color.toHex(): String {
    // toArgb()는 ARGB 순서로 32비트 정수를 반환하므로,
    // 하위 24비트(RGB)만 꺼내서 6자리 16진수로 포맷팅
    val rgb = this.toArgb() and 0x00FFFFFF
    return String.format("#%06X", rgb)
}

fun Map<String, String>.toMultilanguage(defaultLanguage: String) =
    MultiLanguageState(
        mainLanguage = ULocale.forLanguageTag(defaultLanguage),
        initialLanguages = this.keys.map { ULocale.forLanguageTag(it) },
        initialText = this.map { ULocale.forLanguageTag(it.key) to it.value }
    )

fun buildCurrencyRegex(maxDecimals: Int, allowMinus: Boolean): Regex {
    // 1. allowMinus 파라미터 값에 따라 마이너스 기호 정규식을 결정합니다.
    val minusRegex = if (allowMinus) "-?" else ""

    return if (maxDecimals > 0) {
        // --- 소수점을 허용하는 경우 ---
        // --- 수정된 부분: 빈 문자열을 허용하기 위해 (^$)|... 패턴 사용 ---
        // (^$) : 문자열의 시작과 끝이 바로 이어지는 경우, 즉 빈 문자열을 허용합니다.
        // | (OR) : 또는 기존의 통화 형식 패턴을 따릅니다.
        Regex("(^$)|^$minusRegex(?=.*\\d)\\d*(\\.\\d{0,$maxDecimals})?\$")
    } else {
        // --- 정수만 허용하는 경우 ---
        // --- 수정된 부분: 빈 문자열을 허용하기 위해 (^$)|... 패턴 사용 ---
        Regex("(^$)|^$minusRegex\\d+\$")
    }
}

