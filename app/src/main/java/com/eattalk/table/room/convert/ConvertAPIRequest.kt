package com.eattalk.table.room.convert

//import com.eattalk.table.api.request.OptionGroupUpsertRequest
//import com.eattalk.table.api.request.OptionUpsertRequest
//import com.eattalk.table.api.request.SeatRequest
//import com.eattalk.table.api.request.UpdateZonesRequest
//import com.eattalk.table.api.request.UpsertProductRequest
//import com.eattalk.table.api.request.ZoneRequest
//import com.eattalk.table.room.Discount
//import com.eattalk.table.room.OptionGroupWithOptions
//import com.eattalk.table.room.ProductFull
//import com.eattalk.table.room.Seat
//import com.eattalk.table.room.ZoneWithSeats
//
///**
// * Discount 엔티티 → DiscountUpsertRequest 변환용 extension
// */
//fun Discount.toUpsertRequest(): DiscountUpsertRequest =
//    DiscountUpsertRequest(
//        name = this.name,
//        type = this.type,
//        value = this.value.toString()
//    )
//
//fun OptionGroupWithOptions.toUpsertRequest(): OptionGroupUpsertRequest =
//    OptionGroupUpsertRequest(
//        type = optionGroup.type,
//        required = optionGroup.required,
//        maxQuantity = optionGroup.maxQuantity,
//        translations = optionGroup.translations,
//        options = options.map { opt ->
//            OptionUpsertRequest(
//                optionId     = opt.optionId,
//                price        = opt.price.toString(),
//                isDefault    = opt.default,
//                maxQuantity  = opt.maxQuantity,
//                outOfStock   = opt.outOfStock,
//                translations = opt.translations
//            )
//        }
//    )
//
///**
// * ProductFull → UpsertProductRequest 변환용 extension
// */
//fun ProductFull.toUpsertRequest(): UpsertProductRequest =
//    UpsertProductRequest(
//        categoryId      = category?.categoryId,
//        price           = product.price.toString(),
//        imageUrl        = product.imageUrl?.ifEmpty { null },            // Product.resourceId 를 imageUrl 로 사용
//        outOfStock      = product.outOfStock,
//        backgroundColor = product.backgroundColor,
//        optionGroups    = optionGroups.map { it.optionGroup.optionGroupId },
//        tags            = tags.map { it.tag },                          // ProductTag.tagId 필드로 가정
//        translations    = product.translations                          // Product.translations: Map<String,String>
//    )
//
///**
// * ZoneWithSeats → ZoneRequest 변환
// */
//fun ZoneWithSeats.toZoneRequest(): ZoneRequest =
//    ZoneRequest(
//        zoneId = zone.zoneId,
//        name   = zone.name,
//        seats  = seats.map { it.toSeatRequest() }
//    )
//
///**
// * Seat → SeatRequest 변환
// */
//fun Seat.toSeatRequest(): SeatRequest =
//    SeatRequest(
//        seatId = seatId,
//        name   = name,
//        x      = x,
//        y      = y,
//        width  = width,
//        height = height
//    )
//
///**
// * ZoneWithSeats 리스트 → UpdateZonesRequest 변환
// */
//fun List<ZoneWithSeats>.toUpdateZonesRequest(): UpdateZonesRequest =
//    UpdateZonesRequest(
//        zones = this.map { it.toZoneRequest() }
//    )