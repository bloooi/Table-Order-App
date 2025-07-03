package com.eattalk.table.ui.state

import android.icu.util.ULocale
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


class MultiLanguageState(
    val mainLanguage: ULocale,
    initialLanguages: List<ULocale>,
    initialText: List<Pair<ULocale, String>> = emptyList()
) {
    // 내부 상태 저장소: 언어코드를 키로 하는 텍스트 맵
    private val _textMap = mutableStateMapOf(*initialText.toTypedArray())

    // 언어 목록
    private val _languages = mutableStateOf(initialLanguages)

    private val otherLanguages = mutableStateOf(
        initialLanguages.filterNot { it == mainLanguage }
    )

    // 외부에서 읽을 수 있는 언어 목록
    val languages: List<ULocale>
        get() = _languages.value


    // 모든 언어별 텍스트 맵 (읽기 전용)
    val textMap: Map<ULocale, String>
        get() = _textMap.toMap()

    // 특정 언어의 텍스트 가져오기
    fun getText(locale: ULocale): String = _textMap[locale] ?: ""

    // 메인 언어의 텍스트 가져오기
    fun getMainText(): String = _textMap[mainLanguage] ?: ""


    // 특정 언어의 텍스트 설정하기
    fun setText(locale: ULocale, text: String) {
        _textMap[locale] = text
    }

    val inputFieldText: String = "${getMainText()}, ${otherLanguages.value.map { getText(it) }.joinToString { it }}"

    fun clearText() = _textMap.clear()
    
    fun toRequest(): Map<String, String> = _textMap.mapKeys { it.key.language }

    fun copy(): MultiLanguageState {
        // 현재 텍스트 맵의 상태를 Pair 리스트로 변환
        val textMapContent = _textMap.entries.map { (locale, text) ->
            Pair(locale, text)
        }

        // 새 MultiLanguageState 객체 생성
        val newState = MultiLanguageState(
            mainLanguage = mainLanguage,
            initialLanguages = _languages.value,
            initialText = textMapContent
        )

        // _mainLanguage와 otherLanguages도 복사
        newState.otherLanguages.value = otherLanguages.value

        return newState
    }
}

@Composable
fun rememberMultiLanguageState(
    mainLanguage: ULocale,
    initialLanguages: List<ULocale>
): MultiLanguageState {
    return remember { MultiLanguageState(
        mainLanguage = mainLanguage,
        initialLanguages
    ) }
}