package com.eattalk.table.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.eattalk.table.R
import com.eattalk.table.ui.theme.Ref

enum class OrderType (@StringRes val badgeRes: Int, val color: Color, val key: String) {
    Received(R.string.received, Ref.Primary.c300, "RECEIVED"),   // 주문 받음
    Confirmed(R.string.cooking, Ref.Secondary.c400, "CONFIRMED"),   // 요리 중
    Delivered(R.string.completed, Ref.Accent.c400, "DELIVERED"), // 완료 됨
    Canceled(R.string.canceled, Ref.Error.c400, "CANCELLED");    // 취소 됨

    companion object {
        /**
         * Converts a raw String into an OrderedMenuType.
         * Matching is case-insensitive; if no match is found, returns ITEM as default.
         */
        fun from(raw: String?): OrderType {
            return OrderType.entries.firstOrNull { it.name.equals(raw, ignoreCase = true) } ?: Canceled
        }
    }
}
