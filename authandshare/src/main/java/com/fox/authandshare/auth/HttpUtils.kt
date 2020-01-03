package com.fox.authandshare.auth

import android.os.Handler
import android.os.Looper

import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`

import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

/**
 *
 * @author fox.hu
 * @date 2018/8/24
 */

class HttpUtils private constructor() {
    private val mOkHttpClient: OkHttpClient
    private val mDelivery: Handler
    private val mGson: Gson

    init {
        mOkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        mGson = Gson()
        mDelivery = Handler(Looper.getMainLooper())
    }

    private fun getRequest(url: String, callback: ResultCallback<*>) {
        val request = Request.Builder().url(url).build()
        deliveryResult(callback, request)
    }

    private fun postRequest(url: String, callback: ResultCallback<*>, params: List<Param>) {
        val request = buildPostRequest(url, params)
        deliveryResult(callback, request)
    }

    /**
     * 处理结果
     * @param callback
     * @param request
     */
    private fun deliveryResult(callback: ResultCallback<*>, request: Request) {

        mOkHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                sendFailCallback(callback, e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: Response) {
                try {
                    val str = response.body!!.string()
                    if (callback.mType === String::class.java) {
                        /**
                         * 返回字符串
                         */
                        sendSuccessCallBack(callback, str)
                    } else {
                        /**
                         * 这里处理解析返回对象
                         */
                        val `object` = mGson.fromJson<Any>(str, callback.mType)
                        sendSuccessCallBack(callback, `object`)

                    }
                } catch (e: Exception) {
                    //                    LogUtils.e(TAG, "convert json failure", e);
                    sendFailCallback(callback, e)
                }

            }
        })
    }

    private fun sendFailCallback(callback: ResultCallback<*>?, e: Exception) {
        mDelivery.post {
            callback?.onFailure(e)
        }
    }

    private fun sendSuccessCallBack(callback: ResultCallback<*>?, obj: Any) {
        mDelivery.post {
            callback?.onSuccess(obj)
        }
    }

    private fun buildPostRequest(url: String, params: List<Param>): Request {
        val builder = FormBody.Builder()
        for (param in params) {
            builder.add(param.key, param.value)
        }
        val requestBody = builder.build()
        return Request.Builder().url(url).post(requestBody).build()
    }

    /**
     * http请求回调类,回调方法在UI线程中执行
     * @param <T>
    </T> */
    abstract class ResultCallback<T> {

        internal var mType: Type? = null

        init {
            mType = getSuperclassTypeParameter(javaClass)
        }

        /**
         * 请求成功回调
         * @param response
         */
        abstract fun onSuccess(response: T)

        /**
         * 请求失败回调
         * @param e
         */
        abstract fun onFailure(e: Exception)

        companion object {

            internal fun getSuperclassTypeParameter(subclass: Class<*>): Type? {
                val superclass = subclass.genericSuperclass
                if (superclass is Class<*>) {
                    throw RuntimeException("Missing type parameter.")
                }
                val parameterized = superclass as ParameterizedType?
                return `$Gson$Types`.canonicalize(parameterized!!.actualTypeArguments[0])
            }
        }
    }

    /**
     * post请求参数类   这里可以根据项目抽取成泛型
     */
    class Param {

        internal var key: String
        internal var value: String

        constructor() {}

        constructor(key: String, value: String) {
            this.key = key
            this.value = value
        }

    }

    companion object {
        private var mInstance: HttpUtils? = null

        @Synchronized
        private fun getmInstance(): HttpUtils {
            if (mInstance == null) {
                mInstance = HttpUtils()
            }
            return mInstance
        }


        /**********************对外接口 */

        /**
         * get请求
         * @param url  请求url
         * @param callback  请求回调
         */
        operator fun get(url: String, callback: ResultCallback<*>) {
            getmInstance().getRequest(url, callback)
        }

        /**
         * post请求
         * @param url       请求url
         * @param callback  请求回调
         * @param params    请求参数
         */
        fun post(url: String, callback: ResultCallback<*>, params: List<Param>) {
            getmInstance().postRequest(url, callback, params)
        }
    }
}
