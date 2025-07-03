package com.eattalk.table.ui.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BooleanProvider: PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}