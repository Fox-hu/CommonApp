package com.component.kotlintest.app.data.db

import com.component.kotlintest.app.demain.datasource.ForecastDataSource
import com.component.kotlintest.app.demain.model.Forecast
import com.component.kotlintest.app.demain.model.ForecastList
import com.component.kotlintest.app.extensions.*
import org.jetbrains.anko.db.insert

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

    fun saveForecast(forecastList: ForecastList) = forecastDbHelper.use {
        clear(CityForecastTable.NAME)
        clear(DayForecastTable.NAME)

        with(DbDataMapper.convertFromDomain(forecastList)) {
            insert(CityForecastTable.NAME, *map.toVarargArray())
            dailyForecast.forEach { insert(DayForecastTable.NAME, *it.map.toVarargArray()) }
        }
    }
}