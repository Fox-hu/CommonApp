package com.silver.fox.base.component.repository

import androidx.lifecycle.MutableLiveData
import com.fox.network.BaseRequest
import com.fox.network.request.OriResponse
import com.fox.network.request.OriResult
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent

open class BaseRepository : KoinComponent {


    suspend fun <T : Any> executeResponse(
        call: suspend () -> OriResponse<T>
    ): MutableLiveData<OriResult<OriResponse<T>>> {
        return coroutineScope {
            object : BaseRequest<T>() {
                override suspend fun createCall(): OriResponse<T> {
                    return call()
                }
            }.startLoad()
        }
    }
}