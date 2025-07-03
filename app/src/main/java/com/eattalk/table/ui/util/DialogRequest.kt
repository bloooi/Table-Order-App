package com.eattalk.table.ui.util

import androidx.annotation.Keep

@Keep
data class DialogRequest(
    val title: String,
    val body: String,
    val confirm: Pair<String, () -> Unit>,
    val negative: Pair<String, () -> Unit>? = null,
    val isForce: Boolean = false,
)
