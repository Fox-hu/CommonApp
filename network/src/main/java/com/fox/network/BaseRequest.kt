package com.fox.network

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.fox.network.request.OriResponse
import com.fox.network.request.OriResult
import com.silver.fox.ext.logi

/**
 * @Author fox.hu
 * @Date 2020/11/10 13:08
 */
abstract class BaseRequest<Result> {
    private val resourceData: MutableLiveData<OriResult<OriResponse<Result>>> = MutableLiveData()
    private val handler = Handler()
    abstract suspend fun createCall(): OriResponse<Result>

    suspend fun startLoad(): MutableLiveData<OriResult<OriResponse<Result>>> {
        try {
            resourceData.postValue(OriResult.loading(null, "请求中"))
            "status  = ${OriResult.Status.LOADING.name} ,data = null,msg = null".logi("BaseRequest")
            val result = createCall()
            handler.postDelayed({
                if (result.errorCode == -1) {
                    result.status = OriResponse.Status.FAIL
                    "status  = ${OriResult.Status.ACTION_FAIL.name} ,data = ${result},msg = ${result.errorMsg}".logi(
                        "BaseRequest"
                    )
                    resourceData.postValue(OriResult.actionFail(result))
                } else {
                    "status  = ${OriResult.Status.ACTION_SUCCESS.name} ,data = ${result},msg = ${result.errorMsg}".logi(
                        "BaseRequest"
                    )
                    resourceData.postValue(OriResult.actionSuccess(result))
                }
            },50)
        } catch (e: Exception) {
            "status  = ${OriResult.Status.ACTION_ERROR.name} ,data = null,msg = ${e.message}".logi("BaseRequest")
            resourceData.postValue(OriResult.actionError(null, e.message))
        }
        return resourceData
    }
}