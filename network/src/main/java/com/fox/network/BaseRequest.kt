package com.fox.network

import androidx.lifecycle.MutableLiveData
import com.fox.network.request.OriResponse
import com.fox.network.request.OriResult
import kotlinx.coroutines.coroutineScope

/**
 * @Author fox.hu
 * @Date 2020/11/10 13:08
 */
abstract class BaseRequest<Result> {
    protected val resourceData: MutableLiveData<OriResult<OriResponse<Result>>> = MutableLiveData()

    abstract suspend fun createCall(): OriResponse<Result>

    suspend fun startLoad(): MutableLiveData<OriResult<OriResponse<Result>>> {
        coroutineScope {
            resourceData.postValue(OriResult.loading(null, "请求中"))
            try {
                val result = createCall()
                if (result.errorCode ==- 1) {
                    result.status = OriResponse.Status.FAIL
                    resourceData.postValue(OriResult.actionFail(result))
                } else {
                    resourceData.postValue(OriResult.actionSuccess(result))
                }
            } catch (e: Exception) {
                resourceData.postValue(OriResult.actionError(null, e.message))
            }
        }
        return resourceData
    }
}