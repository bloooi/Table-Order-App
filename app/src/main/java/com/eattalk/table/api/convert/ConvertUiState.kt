package com.eattalk.table.api.convert

//import com.eattalk.table.api.response.CustomerEntry
import com.eattalk.table.api.response.StoreSimpleEntry
import com.eattalk.table.ui.state.StoreItemUiState
//
fun StoreSimpleEntry.toUiState(): StoreItemUiState =
    StoreItemUiState(
        id = this.storeId,
        name = this.name,
        address1 = "",
        address2 = ""
    )

//fun CustomerEntry.toUiState(): MembershipItemUiState =
//    MembershipItemUiState(
//        id = customerId,
//        name = name,
//        phone = phoneNumber,
//        email = this.profiles.find { it.type == Instant.PROFILE_EMAIL }?.value ?: "" ,
//        instagram = this.profiles.find { it.type == Instant.PROFILE_INSTAGRAM }?.value ?: "" ,
//        stamps = stamps
//    )