package com.component.kotlintest.demain.datasource

import com.component.kotlintest.demain.model.Forecast
import com.component.kotlintest.demain.model.ForecastList

interface ForecastDataSource {
    fun requestForecastByZipCode(zipCode: Long, date: Long): ForecastList?

    fun requestDayForecast(id: Long): Forecast?
}