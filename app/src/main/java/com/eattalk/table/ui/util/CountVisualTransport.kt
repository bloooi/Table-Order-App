package com.eattalk.table.ui.util

import android.icu.text.DecimalFormat // DecimalFormat을 사용합니다.
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CountVisualTransformation : VisualTransformation {

    // 천 단위 쉼표를 사용하는 DecimalFormat 인스턴스
    private val formatter = DecimalFormat("#,###")

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        // 입력된 텍스트가 비어있거나 숫자로만 구성되지 않은 경우 (예: "-"만 있거나, 잘못된 입력)
        // 또는 이미 포맷팅된 경우 (쉼표가 포함된 경우 - 이 부분은 입력 로직에 따라 달라질 수 있음)
        // 여기서는 단순하게 숫자만 있는 문자열을 가정하고 진행합니다.
        // 실제 입력값(onTextChange로 전달되는 값)은 숫자만 포함하도록 관리해야 합니다.
        if (originalText.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        // 입력된 텍스트(숫자만 있는 문자열이라고 가정)를 숫자로 변환
        val number = try {
            // Long 타입으로 변환하여 큰 수도 처리 (갯수가 매우 클 수 있다면 BigDecimal 고려)
            originalText.toLong()
        } catch (e: NumberFormatException) {
            // 숫자로 변환할 수 없는 경우, 원본 텍스트를 그대로 보여주고 변환 없음
            // 또는 오류 상태를 표시하거나 입력을 막는 로직을 onTextChange에서 처리해야 합니다.
            return TransformedText(text, OffsetMapping.Identity)
        }

        // 숫자를 천 단위 쉼표가 포함된 문자열로 포맷
        val formattedText = formatter.format(number)

        // OffsetMapping 구현
        // 원본 텍스트와 변환된 텍스트 간의 커서 위치 및 선택 영역을 매핑합니다.
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // "12345" -> "12,345"
                // 원본 텍스트에서 숫자가 아닌 문자(쉼표)가 추가된 개수를 계산하여 오프셋 조정
                val commasBeforeOffset = countCommas(formattedText.substring(0, (offset + countCommas(formattedText.substring(0, offset))).coerceAtMost(formattedText.length)))
                return (offset + commasBeforeOffset).coerceIn(0, formattedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // "12,345" -> "12345"
                // 변환된 텍스트에서 쉼표를 제거한 위치로 오프셋 조정
                val commasBeforeOffset = countCommas(formattedText.substring(0, offset))
                return (offset - commasBeforeOffset).coerceIn(0, originalText.length)
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }

    // 주어진 문자열에서 쉼표의 개수를 세는 헬퍼 함수
    private fun countCommas(text: String): Int {
        return text.count { it == ',' }
    }
}