package com.component.kotlintest.data.server

import com.component.kotlintest.data.db.ForecastDb
import com.component.kotlintest.demain.datasource.ForecastDataSource
import com.component.kotlintest.demain.model.Forecast
import com.component.kotlintest.demain.model.ForecastList

class ForecastServer(
    private val dataMapper: ServerDataMapper = ServerDataMapper(),
    private val forecastDb: ForecastDb = ForecastDb()
) :
    ForecastDataSource {

    override fun requestForecastByZipCode(zipCode: Long, date: Long): ForecastList? {
        val result = ForecastByZipCodeRequest(zipCode).execute()
        val converted = dataMapper.convertToDomain(zipCode, result)
        forecastDb.saveForecast(converted)
        return forecastDb.requestForecastByZipCode(zipCode,date)
    }

    override fun requestDayForecast(id: Long): Forecast? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}