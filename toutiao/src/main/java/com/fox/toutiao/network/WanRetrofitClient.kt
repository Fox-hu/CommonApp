package com.fox.toutiao.network

import com.fox.network.BaseRetrofitClient
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.silver.fox.Ktx
import com.silver.fox.ext.isNetWorkAvailable
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import java.io.File

object WanRetrofitClient : BaseRetrofitClient() {

    val service by lazy { getService(WanService::class.java, WanService.BASE_URL) }

    private val cookieJar by lazy {
        PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(Ktx.app)
        )
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        val cacheDir = File(Ktx.app.cacheDir, "responses")
        val cacheSize = 10 * 1024 * 1024L
        val cache = Cache(cacheDir, cacheSize)
        builder.cache(cache).cookieJar(cookieJar).addInterceptor { chain ->
            var request = chain.request()
            if (!Ktx.app.isNetWorkAvailable()) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            }

            var response = chain.proceed(request)
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

}