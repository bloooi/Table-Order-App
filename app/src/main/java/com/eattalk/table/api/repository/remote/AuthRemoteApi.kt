package com.eattalk.table.api.repository.remote

import com.eattalk.table.api.ApiService
import com.eattalk.table.api.repository.AuthApi
import com.eattalk.table.api.request.LoginRequest
import com.eattalk.table.api.response.LoginResponse

import com.eattalk.table.api.util.State
import com.eattalk.table.api.util.requestFlow
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow

class AuthRemoteApi(
    private val commonClient: HttpClient,
    private val client: HttpClient
) : AuthApi {
    private val path = "${ApiService.apiPath}/auth"

    /**
     * 로그인
     *
     * @param request emailAddress, password, platform("POS")
     * @return LoginResponse(sessionId)
     */
    override suspend fun login(request: LoginRequest): Flow<State<LoginResponse>> =
        commonClient.requestFlow(HttpMethod.Post, "$path/login") {
            contentType(Application.Json)
            setBody(request)
        }

//    /**
//     * 회원가입
//     *
//     * @param request name, emailAddress, password
//     * @return SignUpResponse(sessionId or user info)
//     */
//    override suspend fun signup(request: SignUpRequest): Flow<State<HttpResponse>> =
//        client.post("$path/signup") {
//            contentType(Application.Json)
//            setBody(request)
//        }.asFlow()

    /**
     * 로그아웃
     *
     *
     */
    override suspend fun logout(): Flow<State<HttpResponse>> =
        client.requestFlow(HttpMethod.Post, "$path/logout")

}