package com.eattalk.table.hilt

import android.content.Context
import com.eattalk.table.api.ApiService
import com.eattalk.table.api.websocket.KtorWebSocketManager
import com.eattalk.table.api.websocket.WebSocketManager
import com.eattalk.table.ui.util.DialogManager
import com.eattalk.table.util.ResourceKt
import com.eattalk.table.util.SettingsDataStore
import com.eattalk.table.util.Store
import com.eattalk.table.util.StoreDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context) =
        SettingsDataStore(context)

    @Provides
    @Singleton
    fun provideStoreDataStore(@ApplicationContext context: Context) =
        StoreDataStore(context)

    @Provides
    @Singleton
    fun provideResource(@ApplicationContext context: Context) =
        ResourceKt(context)

    @Provides
    @Singleton
    fun provideStoreStateFlow(
        dataStore: StoreDataStore
    ): StateFlow<Store> {
        // DataStore에서 Flow<Store>를 받아와서 한 번만 Hot하게 만들고 공유
        return dataStore.storeFlow
            .stateIn(
                scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
                started = SharingStarted.Eagerly,
                initialValue = Store.empty().copy(currency = "EUR")
            )
    }

    @Provides
    @Singleton
    fun provideSessionIdFlow(
        dataStore: SettingsDataStore
    ): StateFlow<String?> {
        return dataStore.sessionIdFlow
            .stateIn(
                scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
                started = SharingStarted.Eagerly,
                initialValue = null
            )
    }

    @Provides
    @Singleton
    fun provideWebSocketManager(
        apiService: ApiService
    ): WebSocketManager = KtorWebSocketManager(apiService)

    @Provides
    @Singleton
    fun provideMainDialogManager(): DialogManager = DialogManager()

}