package com.eattalk.table.model

import android.icu.math.BigDecimal
import androidx.compose.ui.graphics.Color

/**
 * Represents a product definition.
 */
data class ProductModel(
    val id: String,
    val translation: List<Language>,// 추후 translationID 로 변경
    val resourceId: String,
    val price: BigDecimal,
    val backgroundColor: Color,
    val outOfStock: Boolean,
    val optionGroups: List<OptionGroupModel>,
)