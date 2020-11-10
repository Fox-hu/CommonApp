package com.fox.network


import com.fox.network.adapter.LiveDataCallAdapterFactory
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.silver.fox.Ktx
import com.silver.fox.ext.isNetWorkAvailable
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitClient {

    abstract val serviceMap: Map<Class<*>, Any>

    companion object {
        private const val TIME_OUT = 5
    }

    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            }

            builder.addInterceptor(httpLoggingInterceptor)
                .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

            handleBuilder(builder)
            return builder.build()
        }

    val cookieJar by lazy {
        PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(Ktx.app)
        )
    }

    fun handleBuilder(builder: OkHttpClient.Builder) {
        val cacheDir = File(Ktx.app.cacheDir, "responses")
        val cacheSize = 10 * 1024 * 1024L
        val cache = Cache(cacheDir, cacheSize)
        builder.cache(cache).cookieJar(cookieJar).addInterceptor { chain ->
            var request = chain.request()
            if (!Ktx.app.isNetWorkAvailable()) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            }

            val response = chain.proceed(request)
            if (!Ktx.app.isNetWorkAvailable()) {
                val maxAge = 60 * 60
                response.newBuilder().removeHeader("Pragma")
                    .header("Cache-Control", "public,max-age=$maxAge").build()
            } else {
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder().removeHeader("Pragma")
                    .header("Cache-Control", "public,only-if-cached,max-stale = $maxStale")
            }
            response
        }
    }


    fun <S> createService(serviceClass: Class<S>, baseUrl: String): S =
        Retrofit.Builder().client(client)
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .baseUrl(baseUrl)
            .build()
            .create(serviceClass)

    inline fun <reified T> getService(): T = serviceMap[T::class.java] as T

}