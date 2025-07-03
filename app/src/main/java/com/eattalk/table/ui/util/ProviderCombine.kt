package com.eattalk.table.ui.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

open class ProviderCombine<T, R>(
    private val providerT: PreviewParameterProvider<T>,
    private val providerR: PreviewParameterProvider<R>
) : PreviewParameterProvider<Pair<T, R>> {
    override val values: Sequence<Pair<T, R>>
        get() = providerT.values.zip(providerR.values)
}