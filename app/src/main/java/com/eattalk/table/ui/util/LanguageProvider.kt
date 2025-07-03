package com.eattalk.table.ui.util

import android.icu.util.ULocale
import androidx.compose.ui.tooling.preview.PreviewParameterProvider


class LanguageProvider: PreviewParameterProvider<List<ULocale>> {
    override val values: Sequence<List<ULocale>>
        get() = sequenceOf(
            listOf(
                ULocale("ko"),
                ULocale("en"),
                ULocale("ja"),
            )
        )
}