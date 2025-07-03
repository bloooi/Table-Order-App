package com.eattalk.table.api.util

import androidx.annotation.Keep

@Keep
sealed class State<T> {

    @Keep
    data class Success<T>(val data: T, val key: String?, val date: String?) : State<T>()

    @Keep
    data class Error<T>(val exception: Throwable) : State<T>()


    fun isSuccessful(): Boolean = this is Success

    fun isFailed(): Boolean = this is Error

    companion object {
        /**
         * Returns [State.Success] instance.
         * @param data Data to emit with status.
         */
        fun <T> success(data: T, key: String?, date: String?) =
            Success(data, key, date)

        /**
         * Returns [State.Error] instance.
         * @param exception Exception of failure.
         */
        fun <T> fail(exception: Throwable) =
            Error<T>(exception)
    }
}
