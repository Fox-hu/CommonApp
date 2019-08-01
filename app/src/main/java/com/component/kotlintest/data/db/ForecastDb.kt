package com.component.kotlintest.data.db

import com.component.kotlintest.demain.datasource.ForecastDataSource
import com.component.kotlintest.demain.model.Forecast
import com.component.kotlintest.demain.model.ForecastList
import com.component.kotlintest.extensions.byId
import com.component.kotlintest.extensions.parseList
import com.component.kotlintest.extensions.parseOpt
import org.jetbrains.anko.db.select

class ForecastDb(
    private val forecastDbHelper: ForecastDbHelper = ForecastDbHelper.instance
) : ForecastDataSource {

    override fun requestForecastByZipCode(zipCode: Long, date: Long): ForecastList? = forecastDbHelper.use {
        val dailyRequest = "${DayForecastTable.CITY_ID} = ? AND ${DayForecastTable.DATE} >= ?"
        val dailyForecast = select(DayForecastTable.NAME)
            .whereSimple(dailyRequest, zipCode.toString(), date.toString())
            .parseList { DayForecast(HashMap(it)) }
        val city = select(CityForecastTable.NAME)
            .whereSimple("${CityForecastTable.ID} = ?", zipCode.toString())
            .parseOpt { CityForecast(HashMap(it), dailyForecast) }

        city?.let { DbDataMapper.convertToDomain(it) }
    }

    override fun requestDayForecast(id: Long): Forecast? = forecastDbHelper.use {
        val dayForecast = select(DayForecastTable.NAME).byId(id).parseOpt {
            DayForecast(
                HashMap(it)
            )
        }

        dayForecast?.let { DbDataMapper.convertDayToDomain(it) }
    }

}