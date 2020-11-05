package com.fox.network

data class OriResponse<out T>(val errorCode: Int, val errorMsg: String, val data: T)

sealed class OriResult<out T : Any>(val data: T?){

    data class Success<out T : Any>(val data1: T) : OriResult<T>(data1)
    data class Error(val exception: Exception) : OriResult<Nothing>(null)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data =$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}