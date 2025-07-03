package com.eattalk.table.api.repository

import com.eattalk.table.api.request.CreateStoreRequest
import com.eattalk.table.api.request.UpdateStoreOperationRequest
import com.eattalk.table.api.request.UpdateStoreRequest
import com.eattalk.table.api.response.StoreListResponse
import com.eattalk.table.api.response.StoreOperationResponse
import com.eattalk.table.api.response.StoreResponse
import com.eattalk.table.api.response.StoreWebsocketResponse
import com.eattalk.table.api.util.State
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

interface StoreApi {
    suspend fun createStore(
        request: CreateStoreRequest
    ): Flow<State<StoreResponse>>

    suspend fun updateStore(
        storeId: String,
        request: UpdateStoreRequest
    ): Flow<State<HttpResponse>>

    suspend fun findStoreList(): Flow<State<StoreListResponse>>

    suspend fun findStore(
        storeId: String,
    ): Flow<State<StoreResponse>>

    suspend fun updateStoreOperation(
        storeId: String,
        request: UpdateStoreOperationRequest
    ): Flow<State<StoreOperationResponse>>

    suspend fun websocket(
        storeId: String,
        lastAt: String?
    ): Flow<State<StoreWebsocketResponse>>
}