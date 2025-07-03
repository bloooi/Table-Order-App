package com.eattalk.table.ui.util

import android.icu.math.BigDecimal
import android.icu.text.DecimalFormat // DecimalFormat 임포트 추가
import android.icu.text.NumberFormat
import android.icu.util.Currency
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyVisualTransformation(
    private val defaultCurrency: Currency
) : VisualTransformation {
    private val formatter: NumberFormat = NumberFormat.getNumberInstance().apply {
        isGroupingUsed = true
        minimumFractionDigits = 0
        maximumFractionDigits = defaultCurrency.defaultFractionDigits
    }

    // DecimalFormat으로 안전하게 캐스팅하여 decimalFormatSymbols에 접근
    private val decimalFormatSymbols = (formatter as? DecimalFormat)?.decimalFormatSymbols

    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text

        if (original.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        if (original == "-" || original == ".") {
            return TransformedText(AnnotatedString(original), OffsetMapping.Identity)
        }

        val number = runCatching { BigDecimal(original) }.getOrNull()
            ?: return TransformedText(AnnotatedString(original), OffsetMapping.Identity)

        var formatted = formatter.format(number)
        val isInteger = number.scale() <= 0 || number.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0

        // decimalFormatSymbols가 null이 아닐 때만 후처리 로직 실행
        decimalFormatSymbols?.let { symbols ->
            if (isInteger && defaultCurrency.defaultFractionDigits > 0) {
                val decimalSeparator = symbols.decimalSeparatorString
                val zeroSuffix = decimalSeparator + "0".repeat(defaultCurrency.defaultFractionDigits)

                if (formatted.endsWith(zeroSuffix)) {
                    formatted = formatted.substring(0, formatted.length - zeroSuffix.length)
                }
            } else if (!isInteger && defaultCurrency.defaultFractionDigits > 0) {
                val decimalSeparator = symbols.decimalSeparatorString
                if (formatted.contains(decimalSeparator)) {
                    var tempFormatted = formatted
                    while (tempFormatted.endsWith('0') && tempFormatted.length > tempFormatted.indexOf(decimalSeparator) + 1) {
                        tempFormatted = tempFormatted.dropLast(1)
                    }
                    if (tempFormatted.endsWith(decimalSeparator)) {
                        tempFormatted = tempFormatted.dropLast(decimalSeparator.length)
                    }
                    formatted = tempFormatted
                }
            }
        }

        val origLen = original.length
        val transformedLen = formatted.length

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val newOffset = offset + (transformedLen - origLen)
                return newOffset.coerceIn(0, transformedLen)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val newOffset = offset - (transformedLen - origLen)
                return newOffset.coerceIn(0, origLen)
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}