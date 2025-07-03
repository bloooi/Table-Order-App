package com.eattalk.table.util

/*
* 필요 금액 계산
* - 할인 금액 계산
*   - type 이 Absolute 이면 amount
*   - type 이 Percentage 이면 price * (amount / 100)
* - 상품별 금액 (힐인 포함)
*   - 상품 가격 * 갯수 - (할인 금액 계산 + 할인 금액 계산 + 할인 금액 계산...)
* - 전체 금액 계산 (상품별 할인 미포함)
*   - (상품 가격 * 갯수) + (상품 가격 * 갯수) + ...
* - 전체 금액 계산 (상품별 할인 포함)
*   - (상품 가격 * 갯수 - 할인 총합) + (상품 가격 * 갯수 - 할인 총합) + ...
* - 전체 금액 계산 (상품별 할인, 전체금 할인 포함)
*   - ((상품 가격 * 갯수 - 할인 총합) + (상품 가격 * 갯수 - 할인 총합) + ...) - 총액 할인 총합
* - 촐 할인 금액 계산 (상품별 할인 총액, 전체금 할인 총액 포함)
*   - ((상품 가격 * 갯수) + (상품 가격 * 갯수) + ...) - (((상품 가격 * 갯수 - 할인 총합) + (상품 가격 * 갯수 - 할인 총합) + ...) - 총액 할인 총합)
* */

object DiscountCalculator {

    // 할인 금액 계산
//    fun orderDetailSingleDiscount(
//        price: BigDecimal,
//        currency: Currency,
//        discount: DiscountModel
//    ): BigDecimal =
//        when (discount.unit) {
//            FIXED -> discount.amount
//            PERCENTAGE -> price.multiply(
//                discount.amount.divide(
//                    BigDecimal(100.0),
//                    currency.defaultFractionDigits + 2,
//                    BigDecimal.ROUND_HALF_UP
//                )
//            )
//        }
//
//    // 상품별 금액 (힐인 포함)
//    fun orderDetailPrice(
//        currency: Currency,
//        orderDetail: OrderedMenuUiState,
//        discounts: List<DiscountModel>
//    ): BigDecimal =
//        orderDetail.let {
//            val totalPrice = it.price.multiply(BigDecimal(it.quantity))
//            val calculateDiscounts =
//                discounts.fold(BigDecimal.ZERO) { totalSum, discount -> //할인 다 더하기
//                    totalSum.add(orderDetailSingleDiscount(totalPrice, currency, discount))
//                }
//            // 상품 별 총 가격 산정하고
//            totalPrice.subtract( // 할인 총합을 빼기
//                // 만약 계산한 할인 총합이 상품가격을 넘어가면 상품가격으로 제한
//                if (calculateDiscounts > totalPrice)
//                    totalPrice
//                else
//                    calculateDiscounts
//            )
//        }
//
//    // 전체 금액 계산 (상품별 할인 미포함)
//    fun totalOrderDetailsPriceWithoutDetailDiscount(
//        orders: List<OrderedMenuUiState>,
//    ): BigDecimal =
//        orders.fold(BigDecimal.ZERO) { totalSum, order ->   // 상품 가격 다 더하기
//            totalSum.add(
//                order.price.multiply(BigDecimal(order.quantity))
//            )
//        }
//
//    // 전체 금액 계산 (상품별 할인 포함)
//    fun totalOrderDetailsPrice(
//        currency: Currency,
//        orders: List<OrderedMenuUiState>,
//        discounts: Map<String, List<DiscountModel>>,
//    ): BigDecimal =
//        orders.fold(BigDecimal.ZERO) { totalSum, order ->   // 상품 가격 다 더하기
//            totalSum.add(
//                orderDetailPrice(currency, order, discounts[order.id] ?: emptyList())
//            )
//        }
//
//    // 전체 금액 계산 (상품별 할인, 전체금 할인 포함)
//    fun totalOrderDetailsPriceWithDiscount(
//        currency: Currency,
//        orders: List<OrderedMenuUiState>,
//        discounts: Map<String, List<DiscountModel>>,
//    ): BigDecimal {
//        val totalPrice = totalOrderDetailsPrice(currency, orders, discounts)
//        val calculateDiscounts = discounts[Instant.DISCOUNT_TOTAL_ID]?.fold(BigDecimal.ZERO) { totalSum, discount -> //할인 다 더하기
//            totalSum.add(orderDetailSingleDiscount(totalPrice, currency, discount))
//        } ?: BigDecimal.ZERO
//        return totalPrice.subtract(
//            // 만약 계산한 할인 총합이 전체 가격을 넘어가면 전체가격으로 제한
//            if (calculateDiscounts > totalPrice)
//                totalPrice
//            else
//                calculateDiscounts
//        )
//    }
//
//    // 촐 할인 금액 계산 (상품별 할인 총액, 전체금 할인 총액 포함)
//    fun totalDiscountPrice(
//        currency: Currency,
//        orders: List<OrderedMenuUiState>,
//        discounts: Map<String, List<DiscountModel>>,
//    ): BigDecimal = totalOrderDetailsPriceWithoutDetailDiscount(orders) // 전체 금액 계산 (상품별 할인 미포함)
//        .subtract(
//            totalOrderDetailsPriceWithDiscount(
//                currency,
//                orders,
//                discounts
//            )
//        )  // 빼기 전체 금액 계산 (상품별 할인, 전체금 할인 포함)
//
//    fun tipPrice(
//        percentage: BigDecimal,
//        currency: Currency,
//        orders: List<OrderedMenuUiState>,
//        discounts: Map<String, List<DiscountModel>>,
//        ): BigDecimal = totalOrderDetailsPriceWithDiscount(
//            currency = currency,
//            orders = orders,
//            discounts = discounts
//        ).multiply(percentage).setScale(currency.defaultFractionDigits, BigDecimal.ROUND_HALF_UP)
}