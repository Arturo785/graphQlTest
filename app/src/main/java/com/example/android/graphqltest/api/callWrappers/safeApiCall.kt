package com.example.android.graphqltest.api.callWrappers

import android.util.Log
import com.apollographql.apollo.exception.ApolloException

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.io.IOException

// class used for catching the errors from the request and making them able to be handled
// Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ResultWrapper.GenericError(code, "NETWORK_ERROR_TIMEOUT")
                }
                is IOException -> {
                    val error = throwable.message
                    ResultWrapper.NetworkError(error)
                    //ResultWrapper.NetworkErrorTest // just return the state
                }
                is ApolloException -> {
                    val errorResponse = convertErrorBody(throwable)
                    ResultWrapper.GenericError(null, errorResponse)
                }
                else -> {
                    val message = "${throwable.cause} :  ${throwable.message}"
                    ResultWrapper.GenericError(null, message)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: ApolloException): String? {
    return try {
        throwable.cause?.message
    } catch (exception: Exception) {
        "UNKNOWN_ERROR"
    }
}