package com.eattalk.table.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(Instant.DATA_STORE)


    private object Keys {
        val SESSION_ID = stringPreferencesKey(Instant.DS_SESSION_ID)
        val ORDER_PRINTER_ID = stringPreferencesKey(Instant.DS_ORDER_PRINTER_ID)
        val WEBSOCKET_LAST_AT_ID = stringPreferencesKey(Instant.DS_WEBSOCKET_LAST_AT)
    }

    suspend fun saveSession(sessionId: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SESSION_ID] = sessionId
        }
    }

    suspend fun saveOrderPrinter(
        type: String,
        key: String,
    ) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ORDER_PRINTER_ID] = "$type${Instant.PRINTER_SPLIT_CODE}$key"
        }
    }

    suspend fun saveWebsocketLastAt(
        lastAt: String,
    ) {
        context.dataStore.edit { prefs ->
            prefs[Keys.WEBSOCKET_LAST_AT_ID] = lastAt
        }
    }

    // 읽기
    val sessionIdFlow: Flow<String> =
        context.dataStore.data.map { it[Keys.SESSION_ID] ?: "" }.distinctUntilChanged()
    val orderPrinterIdFlow: Flow<String> =
        context.dataStore.data.map { it[Keys.ORDER_PRINTER_ID] ?: "" }.distinctUntilChanged()
    val websocketLastAtFlow: Flow<String?> =
        context.dataStore.data.map { it[Keys.WEBSOCKET_LAST_AT_ID] }.distinctUntilChanged()

    suspend fun deleteAll() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.SESSION_ID)
            prefs.remove(Keys.ORDER_PRINTER_ID)
            prefs.remove(Keys.WEBSOCKET_LAST_AT_ID)
        }
    }

    suspend fun deleteLastAt(){
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.WEBSOCKET_LAST_AT_ID)
        }
    }
}