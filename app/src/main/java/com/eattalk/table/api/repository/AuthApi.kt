package com.eattalk.table.api.repository

import com.eattalk.table.api.request.LoginRequest
import com.eattalk.table.api.response.LoginResponse
import com.eattalk.table.api.util.State
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

interface AuthApi {
    suspend fun login(request: LoginRequest): Flow<State<LoginResponse>>
//    suspend fun signup(request: SignUpRequest): Flow<State<HttpResponse>>
    suspend fun logout(): Flow<State<HttpResponse>>

}