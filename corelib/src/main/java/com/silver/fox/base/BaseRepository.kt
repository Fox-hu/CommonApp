package com.silver.fox.base

import com.fox.network.MyResponse
import com.fox.network.MyResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import java.io.IOException

open class BaseRepository : KoinComponent {

    suspend fun <T : Any> apiCall(call: suspend () -> MyResponse<T>): MyResponse<T> = call.invoke()

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> MyResult<T>,
        errorMsg: String
    ): MyResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            MyResult.Error(IOException(errorMsg, e))
        }
    }

    suspend fun <T : Any> executeResponse(
        response: MyResponse<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): MyResult<T> {
        return coroutineScope {
            if (response.errorCode == -1) {
                errorBlock?.let { it() }
                MyResult.Error(IOException(response.errorMsg))
            } else {
                successBlock?.let { it() }
                MyResult.Success(response.data)
            }
        }
    }
}