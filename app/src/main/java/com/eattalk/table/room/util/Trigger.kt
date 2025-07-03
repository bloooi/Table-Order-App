@file:OptIn(FlowPreview::class)

package com.eattalk.table.room.util

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.merge
import kotlinx.datetime.Clock

class Trigger {
    val zoneTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val seatTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val orderSessionTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val orderTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val orderDetailTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val orderDetailOptionTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val optionGroupTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val optionTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val productTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val productOptionGroupTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val productTagTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val categoryTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val categoryProductTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val discountTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val orderDetailDiscountTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val orderSessionDiscountTrigger = MutableStateFlow(Clock.System.now().toEpochMilliseconds())


    suspend fun updateZoneTrigger() {
        zoneTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateSeatTrigger() {
        seatTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateOrderSessionTrigger() {
        orderSessionTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateOrderTrigger() {
        orderTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateOrderDetailTrigger() {
        orderDetailTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateOrderDetailOptionTrigger() {
        orderDetailOptionTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateOptionGroupTrigger() {
        optionGroupTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateOptionTrigger() {
        optionTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateProductTrigger() {
        productTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateProductOptionGroupTrigger() {
        productOptionGroupTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateProductTagTrigger() {
        productTagTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateCategoryTrigger() {
        categoryTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateCategoryProductTrigger() {
        categoryProductTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateDiscountTrigger() {
        discountTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateOrderDetailDiscountTrigger() {
        orderDetailDiscountTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

    suspend fun updateOrderSessionDiscountTrigger() {
        orderSessionDiscountTrigger.emit(Clock.System.now().toEpochMilliseconds())
    }

}

fun mergeTrigger(
    vararg flows: Flow<Long>,
): Flow<Long> =
    merge(*flows)
        .distinctUntilChanged()
        .debounce(300L)