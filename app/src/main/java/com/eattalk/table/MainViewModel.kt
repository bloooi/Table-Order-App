
package com.eattalk.table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eattalk.table.ui.util.DialogManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val dialogManager = DialogManager()

    companion object {
        // 기획서 기준 5분. 테스트를 위해 짧게 설정할 수도 있습니다.
        private const val IDLE_TIMEOUT_MS = 5 * 60 * 1000L
    }

    private val _timeoutEvent = MutableSharedFlow<Unit>()
    val timeoutEvent = _timeoutEvent.asSharedFlow()

    private var idleTimerJob: Job? = null

    init {
        startIdleTimer()
    }

    /**
     * 사용자 상호작용이 있을 때마다 호출되어 타임아웃 타이머를 리셋합니다.
     */
    fun resetIdleTimer() {
        idleTimerJob?.cancel()
        startIdleTimer()
    }

    private fun startIdleTimer() {
        idleTimerJob = viewModelScope.launch {
            delay(IDLE_TIMEOUT_MS)
            _timeoutEvent.emit(Unit)
        }
    }

    override fun onCleared() {
        idleTimerJob?.cancel()
        super.onCleared()
    }
}
