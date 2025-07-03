package com.eattalk.table.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eattalk.table.api.repository.StoreApi
import com.eattalk.table.api.util.apiCollect
import com.eattalk.table.room.repository.StoreRepository
import com.eattalk.table.ui.util.DialogManager
import com.eattalk.table.ui.util.DialogRequest
import com.eattalk.table.util.Instant
import com.eattalk.table.util.ResourceKt
import com.eattalk.table.util.SettingsDataStore
import com.eattalk.table.util.Store
import com.eattalk.table.util.StoreDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.toKotlinInstant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val storeFlow: StateFlow<Store>,
    val dataStore: SettingsDataStore,
    val storeApi: StoreApi,
    val resource: ResourceKt,
    val settingsDataStore: SettingsDataStore,
    val storeDataStore: StoreDataStore,
    val storeRepository: StoreRepository,
) : ViewModel() {
    val dialogManager = DialogManager()
    val _goToMain: MutableSharedFlow<Unit> = MutableSharedFlow(0)
    val goToMain: SharedFlow<Unit> = _goToMain.asSharedFlow()

    val _goToStart: MutableSharedFlow<Unit> = MutableSharedFlow(0)
    val goToStart: SharedFlow<Unit> = _goToStart.asSharedFlow()

    val _finish: MutableSharedFlow<Unit> = MutableSharedFlow(0)
    val finish: SharedFlow<Unit> = _finish.asSharedFlow()

    fun checkSession() {
        viewModelScope.launch {
            when {
                dataStore.sessionIdFlow.first().isEmpty() -> _goToStart.emit(Unit)
                storeFlow.value.storeId.isEmpty() -> {
                    _goToStart.emit(Unit)
                }

                else -> refreshAllData(storeFlow.value.storeId)
            }
        }
    }

    fun refreshAllData(storeId: String) {
        viewModelScope.launch {
            storeApi.findStore(storeId).apiCollect(
                success = {
                    val entry = it.data.store
                    storeDataStore.saveStore(
                        Store(
                            storeId = entry.storeId,
                            name = entry.name,
                            imageUrl = entry.imageUrl,
                            defaultLanguage = entry.defaultLanguage,
                            currency = entry.currency,
                            supportLanguages = entry.supportLanguages,
                            taxRate = entry.taxRate,
                            operation = entry.operation,
                            openedAt = entry.openedAt,
                            closedAt = entry.closedAt,
                            stamps = entry.stamps
                        )
                    )
                    withContext(Dispatchers.IO) {
                        storeRepository.syncAllDataFromServer(storeId, entry)
                    }
                    it.date?.let { lastAt ->
                        val formatter = DateTimeFormatter.ofPattern(
                            Instant.RESPONSE_DATE_FORMAT,
                            Locale.ENGLISH
                        )
                        val lastAtTime = ZonedDateTime.parse(lastAt, formatter)
                            .toInstant()
                            .toKotlinInstant()
                            .toString()
                        settingsDataStore.saveWebsocketLastAt(lastAtTime)
                    }
                    _goToMain.emit(Unit)
                },
                error = {
                    dialogManager.showDialog(
                        DialogRequest(
                            title = resource.errorDefaultTitle,
                            body = resource.errorDefaultBody,
                            confirm = resource.close to {
                                dialogManager.dismissDialog()
                                viewModelScope.launch {
                                    _finish.emit(Unit)
                                }
                            },
                        )
                    )
                }
            )
        }
    }

}