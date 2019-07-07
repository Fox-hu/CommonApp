package com.component.kotlintest.data.db

import com.component.kotlintest.demain.datasource.ForecastDataSource
import com.component.kotlintest.demain.model.Forecast
import com.component.kotlintest.demain.model.ForecastList

class ForecastDb(
    private val forecastDbHelper: ForecastDbHelper = ForecastDbHelper.instance,
    private val dataMapper: DbDataMapper = DbDataMapper()
) : ForecastDataSource {

    override fun requestForecastByZipCode(zipCode: Long, data: Long): ForecastList? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestDayForecast(id: Long): Forecast? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}