package com.eattalk.table.api.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val loginSessionId: String,
)
