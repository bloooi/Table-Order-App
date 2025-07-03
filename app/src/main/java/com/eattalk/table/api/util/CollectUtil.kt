package com.eattalk.table.api.util

import com.eattalk.table.api.response.EatTalkException
import kotlinx.coroutines.flow.Flow

suspend fun <T> Flow<State<T>>.apiCollect(
    success: (suspend (State.Success<T>) -> Unit)? = null,
    responseError: (suspend (State.Error<T>) -> Unit)? = null,
    clientError: (suspend (State.Error<T>) -> Unit)? = null,
) {
    this.collect { state ->
        when (state) {
            is State.Error -> {
                when (state.exception) {
                    is EatTalkException -> responseError?.invoke(state)
                    else -> clientError?.invoke(state)
                }
            }

            is State.Success -> {
                try {
                    success?.invoke(state)
                } catch (e: Exception) {
//                    CrashlyticsUtil.errorLog(e)
                    responseError?.invoke(State.Error(e))
                    e.printStackTrace()
                }
            }
        }
    }
}

suspend fun <T> Flow<State<T>>.apiCollect(
    success: (suspend (State.Success<T>) -> Unit)? = null,
    error: (suspend (State.Error<T>) -> Unit)? = null,
) {
    this.collect { state ->
        when (state) {
            is State.Error -> {
                error?.invoke(state)
            }

            is State.Success -> {
                try {
                    success?.invoke(state)
                } catch (e: Exception) {
//                    CrashlyticsUtil.errorLog(e)
                    error?.invoke(State.Error(e))
                    e.printStackTrace()
                }
            }
        }
    }
}
