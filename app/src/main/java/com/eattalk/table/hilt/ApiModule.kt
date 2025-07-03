package com.eattalk.table.hilt

import com.eattalk.table.api.ApiService
import com.eattalk.table.api.repository.AuthApi
import com.eattalk.table.api.repository.StoreApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Singleton
import kotlin.String

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun providesApiService(
        sessionIdFlow: StateFlow<String?>,
    ): ApiService = ApiService(
        sessionIdFlow,

    )

    @Provides
    @Singleton
    fun authApi(
        apiService: ApiService
    ): AuthApi = apiService.authApi()

    @Provides
    @Singleton
    fun storeApi(
        apiService: ApiService
    ): StoreApi = apiService.storeApi()

}