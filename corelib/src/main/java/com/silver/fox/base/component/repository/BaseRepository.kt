package com.silver.fox.base.component.repository

import com.fox.network.OriResponse
import com.fox.network.OriResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent

open class BaseRepository : KoinComponent {

    suspend fun <T : Any> apiCall(call: suspend () -> OriResponse<T>): OriResponse<T> = call.invoke()

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> OriResult<T>,
        errorMsg: String
    ): OriResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            OriResult.Error(Exception(errorMsg, e))
        }
    }

    suspend fun <T : Any> executeResponse(
        response: OriResponse<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): OriResult<T> {
        return coroutineScope {
            if (response.errorCode == -1) {
                errorBlock?.let { it() }
                OriResult.Error(Exception(response.errorMsg))
            } else {
                successBlock?.let { it() }
                OriResult.Success(response.data)
            }
        }
    }
}