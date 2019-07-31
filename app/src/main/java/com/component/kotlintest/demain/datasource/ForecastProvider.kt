package com.component.kotlintest.demain.datasource

import com.component.kotlintest.data.db.ForecastDb
import com.component.kotlintest.extensions.firstResult

class ForecastProvider(private val sources: List<ForecastDataSource> = ForecastProvider.SOURCES) {

    companion object {
        const val DAY_IN_MILLIS = 1000 * 60 * 60 * 24
        val SOURCES by lazy { listOf(ForecastDb()) }
        val MAPS by lazy { hashMapOf<String, ForecastDataSource>("db" to ForecastDb()) }
    }

    fun requestByZipCode(zipCode: Long, days: Int) = requestToSources {
        val result = it.requestForecastByZipCode(zipCode, todayTimeSpan())
        if (result != null && result.size > days) result else null
    }

    private fun todayTimeSpan() = System.currentTimeMillis() / DAY_IN_MILLIS * DAY_IN_MILLIS

    private fun <T : Any> requestToSources(f: (ForecastDataSource) -> T?): T = sources.firstResult { f(it) }

//    fun requestByZipCode1(zipCode: Long, days: Int) = requestToSources1 {
//
//    }
//
//    private fun <T : Any> requestToSources1(f: (String, ForecastDataSource) -> T?): T = MAPS.firstResult { f() }
}