package com.fox.network.request

/**
 * @Author fox.hu
 * @Date 2020/11/10 14:40
 */
class OriResult<T> private constructor(
    val status: Status, val data: T?,
    val message: String?
) {
    /**
     * 数据的状态
     *
     *
     * 可以将数据的加载过程分成三个阶段
     * 1. 开始加载
     * 2. 加载中
     * 3. 加载完成
     *
     *
     * 但客户端感兴趣只有两类状态
     * 1. 开始加载-此时通常需要弹等待框
     * 2. 加载完成-此时需要根据加载到的数据去更新页面状态（正常，异常，错误），更新页面数据（正常，异常，错误）
     *
     *
     * 所以Status包含两类状态
     * 1. 开始加载/重新加载-Loading/Reloading
     * 2. 加载完成-Success/Fail/Error
     *
     *
     * 加载完成时的三种状态之间的区别：
     * 1. Success，代表成功读取到资源，并且结果是符合预期的（如成功换取到数据，成功更新了数据。在51中对应stateCode = 1）
     * 2. Fail，代表成功读取到资源，但是结果是不符合预期的（如参数错误，登录信息错误等。在51中对应stateCode != 1）
     * 3. Error，代表未成功读取到资源，如资源不存在（404），ssl错误，网络错误等
     */
    enum class Status {
        /**
         * 数据加载中，通常在开始请求资源时回调一次
         */
        LOADING,

        /**
         * 接口返回的数据是正常的
         */
        ACTION_SUCCESS,

        /**
         * 接口返回的数据是不正常的，通常是服务器觉得我们传递的参数存在异常
         */
        ACTION_FAIL,

        /**
         * 读取接口数据的过程中发生错误
         */
        ACTION_ERROR
    }

    companion object {

        fun <T> actionSuccess(data: T?): OriResult<T> {
            return OriResult(
                Status.ACTION_SUCCESS,
                data,
                getMessage(data)
            )
        }

        fun <T> actionFail(data: T?): OriResult<T> {
            return OriResult(
                Status.ACTION_FAIL,
                data,
                getMessage(data)
            )
        }

        fun <T> actionError(data: T?, msg: String?): OriResult<T> {
            return OriResult(Status.ACTION_ERROR, data, msg)
        }

        fun <T> loading(data: T?, msg: String?): OriResult<T> {
            return OriResult(Status.LOADING, data, msg)
        }

        fun <T> getMessage(data: T): String {
            var message = ""
            if (data != null) {
                if (data is OriResponse<*>) {
                    message = (data as OriResponse<*>).errorMsg
                }
            }
            return message
        }
    }

    fun <T>copyIgnoreData(data:T):OriResult<T> = OriResult<T>(this.status,data,this.message)

}