package com.eattalk.table.ui.util


sealed interface UiLoadingState<out T> {
    /** 로딩중 */
    object Loading : UiLoadingState<Nothing>

    /** 성공: data 를 담아서 전파 */
    data class Success<out T>(val uiState: T) : UiLoadingState<T>

    /** 에러: Code 만 담아서 전파 */
    data class Error(val code: String) : UiLoadingState<Nothing>
}