package com.eattalk.table.model

/**
 * Represents a group of options available for a product.
 */
data class OptionGroupModel(
    val id: String,
    val translation: List<Language>,// 추후 translationID 로 변경
    val type: String,
    val required: Boolean,
    val maxQuantity: Int,
    val options: List<OptionModel>
)