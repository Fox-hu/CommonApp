package com.fox.network.request

/**
 * @Author fox.hu
 * @Date 2020/11/9 17:01
 */
enum class OriState {
    /**
     * 数据加载中，通常在开始请求资源时回调一次
     */
    LOADING,


    /**
     * 读取的缓存中的数据是有效的
     */
    CACHE_SUCCESS,

    /**
     * 读取的缓存中的数据是无效的
     */
    CACHE_FAIL,

    /**
     * 读取缓存时发生错误
     */
    CACHE_ERROR,

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