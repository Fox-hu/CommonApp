package com.component.kotlintest.app.demain.datasource

import com.component.kotlintest.app.demain.model.Forecast
import com.component.kotlintest.app.demain.model.ForecastList

interface ForecastDataSource {
    fun requestForecastByZipCode(zipCode: Long, date: Long): ForecastList?

    fun requestDayForecast(id: Long): Forecast?
}