package com.eattalk.table.model

import kotlinx.serialization.Serializable

@Serializable
enum class OperationState {
    OPENED, PAUSED, RESUMED, CLOSED
}