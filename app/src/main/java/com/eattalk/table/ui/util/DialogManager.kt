package com.eattalk.table.ui.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class DialogManager(
//    val instant: Instant
) {
    private val _dialogQueue = MutableStateFlow<List<DialogRequest>>(emptyList())
    val dialogQueue: StateFlow<List<DialogRequest>> = _dialogQueue.asStateFlow()

    fun showDialog(request: DialogRequest) {
        _dialogQueue.value += request // 대기열에 추가
    }

    fun dismissDialog() {
        _dialogQueue.value = _dialogQueue.value.drop(1) // 가장 첫 요청 제거
    }

//    fun networkErrorRequest(
//        onClose: () -> Unit = { dismissDialog() }
//    ): DialogRequest =
//        DialogRequest(
//            instant.errorCommonTitle,
//            instant.errorNetwork,
//            ButtonData(instant.close, onClose)
//        )
}