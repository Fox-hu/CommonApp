package com.fox.network.log

import com.silver.fox.ext.logi
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @Author fox.hu
 * @Date 2020/11/12 9:52
 */
class HttpLogger :HttpLoggingInterceptor.Logger{
    override fun log(message: String) {
        message.logi("Http")
    }
}