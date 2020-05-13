package com.silver.fox.base.component.repository

import com.fox.network.Response
import com.fox.network.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import java.io.IOException

open class BaseRepository : KoinComponent {

    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Response<T> = call.invoke()

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Result<T>,
        errorMsg: String
    ): Result<T> {
        return try {
            call()
        } catch (e: Exception) {
            Result.Error(IOException(errorMsg, e))
        }
    }

    suspend fun <T : Any> executeResponse(
        response: Response<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): Result<T> {
        return coroutineScope {
            if (response.errorCode == -1) {
                errorBlock?.let { it() }
                Result.Error(IOException(response.errorMsg))
            } else {
                successBlock?.let { it() }
                Result.Success(response.data)
            }
        }
    }
}