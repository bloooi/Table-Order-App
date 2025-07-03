package com.eattalk.table.model

import android.icu.math.BigDecimal

/**
 * Represents a single selectable option within an option group.
 */
data class OptionModel(
    val id: String,
    val groupId: String,
    val translation: List<Language>,// 추후 translationID 로 변경
    val price: BigDecimal,
    val sequenceInDisplay: Int,
    val isDefault: Boolean,
    val maxQuantity: Int,
    val outOfStock: Boolean
)