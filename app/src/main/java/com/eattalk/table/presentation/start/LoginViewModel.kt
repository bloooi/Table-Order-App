package com.eattalk.table.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eattalk.table.api.convert.toUiState
import com.eattalk.table.api.repository.AuthApi
import com.eattalk.table.api.repository.StoreApi
import com.eattalk.table.api.request.LoginRequest
import com.eattalk.table.api.util.apiCollect
import com.eattalk.table.room.repository.StoreRepository
import com.eattalk.table.ui.state.StoreItemUiState
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthApi,
    private val storeApi: StoreApi,
    private val dataStore: SettingsDataStore,
    val sessionIdFlow: StateFlow<String?>,
    val storeDataStore: StoreDataStore,
    val storeFlow: StateFlow<Store>,
    val resource: ResourceKt,
    val storeRepository: StoreRepository,
) : ViewModel() {
    val dialogManager = DialogManager()
    private val _finishLogin = MutableSharedFlow<Unit>(1)
    val finishLogin: SharedFlow<Unit> = _finishLogin.asSharedFlow()

    val email: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")

    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val stores: MutableStateFlow<List<StoreItemUiState>?> = MutableStateFlow(null)


    fun onEmailChange(value: String) {
        email.value = value
    }

    fun onPasswordChange(value: String) {
        password.value = value
    }

    // 0 : Login, 1 : Select Store
    val uiStep: MutableStateFlow<Int> =
        MutableStateFlow(if (sessionIdFlow.value.isNullOrEmpty()) 0 else 1)

    init {
        viewModelScope.launch {
            uiStep.collect {
                if (it == 1) {
                    storeList()
                }
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            isLoading.emit(true)
            authRepo.login(LoginRequest(email.value, password.value, Instant.platform))
                .apiCollect(
                    responseError = {
                        isLoading.emit(false)
                        dialogManager.showDialog(
                            DialogRequest(
                                title = resource.errorValidationTitle,
                                body = resource.errorValidationBody,
                                confirm = resource.close to { dialogManager.dismissDialog() },
                                isForce = false
                            )
                        )
                    },

                    clientError = {
                        isLoading.emit(false)
                        dialogManager.showDialog(
                            DialogRequest(
                                title = resource.errorDefaultTitle,
                                body = resource.errorDefaultBody,
                                confirm = resource.close to { dialogManager.dismissDialog() },
                            )
                        )
                    },
                    success = {
                        isLoading.emit(false)
                        dataStore.saveSession(it.data.loginSessionId)
                        uiStep.emit(1)
                    }
                )
        }
    }


    fun logout() {
        viewModelScope.launch {
            authRepo.logout()
                .apiCollect(
                    success = {
                        dataStore.deleteAll()
                        uiStep.emit(0)
                    },
                    error = {
                        dataStore.deleteAll()
                        uiStep.emit(0)
                    }
                )
        }
    }

    fun storeList() {
        viewModelScope.launch {
            isLoading.emit(true)
            storeApi.findStoreList().apiCollect(
                responseError = {
                    isLoading.emit(false)
                    dialogManager.showDialog(
                        DialogRequest(
                            title = resource.errorDefaultTitle,
                            body = resource.errorDefaultBody,
                            confirm = resource.close to { dialogManager.dismissDialog() },
                            isForce = false
                        )
                    )
                },
                clientError = {
                    isLoading.emit(false)
                    dialogManager.showDialog(
                        DialogRequest(
                            title = resource.errorDefaultTitle,
                            body = resource.errorDefaultBody,
                            confirm = resource.close to { dialogManager.dismissDialog() },
                        )
                    )
                },
                success = {
                    isLoading.emit(false)
                    stores.value = it.data.stores.map { it.toUiState() }
                }
            )
        }
    }

    fun selectStore(storeId: String) {
        viewModelScope.launch {
            isLoading.emit(true)
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
                        storeRepository.syncAllDataFromServer(storeFlow.value.storeId, entry)
                    }
                    
                    _finishLogin.emit(Unit)
                }
            )
        }
    }

}