package com.eattalk.table.api.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("emailAddress")
    val email: String,
    val password: String,
    val platform: String
)
