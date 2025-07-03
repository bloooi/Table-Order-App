package com.eattalk.table.api.repository.remote

import com.eattalk.table.api.ApiService
import com.eattalk.table.api.repository.StoreApi
import com.eattalk.table.api.request.CreateStoreRequest
import com.eattalk.table.api.request.UpdateStoreOperationRequest
import com.eattalk.table.api.request.UpdateStoreRequest
import com.eattalk.table.api.request.WebSocketRequest
import com.eattalk.table.api.response.StoreListResponse
import com.eattalk.table.api.response.StoreOperationResponse
import com.eattalk.table.api.response.StoreResponse
import com.eattalk.table.api.response.StoreWebsocketResponse

import com.eattalk.table.api.util.State

import com.eattalk.table.api.util.requestFlow
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow

class StoreRemoteApi(
    private val client: HttpClient
): StoreApi {
    private val storePath = "/stores"
    private val webSocketPath = "/websocket"
    /**
    * POST /api/pos/stores
    * */
    override suspend fun createStore(request: CreateStoreRequest): Flow<State<StoreResponse>> =
        client.requestFlow(HttpMethod.Post, "${ApiService.apiPath}$storePath") {
            contentType(Application.Json)
            setBody(request)
        }

    /**
     * PATCH /api/pos/stores/:store_id
     * */
    override suspend fun updateStore(
        storeId: String,
        request: UpdateStoreRequest
    ): Flow<State<HttpResponse>> =
        client.requestFlow(HttpMethod.Patch, "${ApiService.apiPath}$storePath/$storeId") {
            contentType(Application.Json)
            setBody(request)
        }


    /**
    * GET /api/pos/stores
    * */
    override suspend fun findStoreList(): Flow<State<StoreListResponse>> =
        client.requestFlow(HttpMethod.Get, "${ApiService.apiPath}$storePath")
            

    /**
     * GET /api/pos/stores/:store_id
     *
     * */
    override suspend fun findStore(storeId: String): Flow<State<StoreResponse>> =
        client.requestFlow(HttpMethod.Get, "${ApiService.apiPath}$storePath/$storeId")
            

    /**
     * PATCH /api/pos/stores/:store_id/operation
     * */
    override suspend fun updateStoreOperation(
        storeId: String,
        request: UpdateStoreOperationRequest
    ): Flow<State<StoreOperationResponse>> =
        client.requestFlow(HttpMethod.Patch, "${ApiService.apiPath}$storePath/$storeId/operation") {
            contentType(Application.Json)
            setBody(request)
        }

    override suspend fun websocket(storeId: String, lastAt: String?): Flow<State<StoreWebsocketResponse>> =
        client.requestFlow(HttpMethod.Post, "${ApiService.apiPath}$storePath/$storeId$webSocketPath"){
            contentType(Application.Json)
            setBody(WebSocketRequest(lastAt))
        }
}