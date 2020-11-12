package com.fox.network.request

data class OriResponse<T>(
    var status: Status = Status.NORMAL,
    val errorCode: Int,
    val errorMsg: String,
    var data: T?
) {
    enum class Status {
        NORMAL, FAIL, ERROR
    }

    companion object {
        fun <T> Fail(): OriResponse<T> {
            return OriResponse(Status.FAIL, -1, "加载错误", null)
        }

        fun <T> Error(string: String?): OriResponse<T> {
            return OriResponse(Status.ERROR, -2, string ?: "加载失败", null)
        }
    }
}

