package com.example.android.graphqltest.api.callWrappers

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val errorMessage: String? = null): ResultWrapper<Nothing>()
    data class NetworkError(val cause: String? = null): ResultWrapper<Nothing>()
}