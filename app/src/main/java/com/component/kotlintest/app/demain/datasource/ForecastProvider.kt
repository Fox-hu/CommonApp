package com.component.kotlintest.app.demain.datasource

import com.component.kotlintest.app.data.db.ForecastDb
import com.component.kotlintest.app.data.server.ForecastServer
import com.component.kotlintest.app.demain.model.Forecast
import com.component.kotlintest.app.extensions.firstResult

class ForecastProvider(private val sources: List<ForecastDataSource> = ForecastProvider.SOURCES) {

    companion object {
        const val DAY_IN_MILLIS = 1000 * 60 * 60 * 24
        val SOURCES by lazy { listOf(ForecastDb(), ForecastServer()) }
        val MAPS by lazy { hashMapOf<String, ForecastDataSource>("db" to ForecastDb()) }
    }

    fun requestByZipCode(zipCode: Long, days: Int) = requestToSources {
        val result = it.requestForecastByZipCode(zipCode, todayTimeSpan())
        if (result != null && result.size >= days) result else null
    }

    fun requestForecast(id:Long):Forecast = requestToSources {
        it.requestDayForecast(id)
    }

    private fun todayTimeSpan() = System.currentTimeMillis() / DAY_IN_MILLIS * DAY_IN_MILLIS

    private fun <T : Any> requestToSources(f: (ForecastDataSource) -> T?): T = sources.firstResult { f(it) }

    fun requestByZipCodeWithMap(zipCode: Long, days: Int) = requestToSourcesWithMap { s, forecastDataSource ->
        val result = forecastDataSource.requestForecastByZipCode(zipCode, todayTimeSpan())
        if (result != null && result.size > days) result else null
    }

    private fun <T : Any> requestToSourcesWithMap(f: (String, ForecastDataSource) -> T?): T =
        MAPS.firstResult { s: String, forecastDataSource: ForecastDataSource ->
            f(s, forecastDataSource)
        }
}