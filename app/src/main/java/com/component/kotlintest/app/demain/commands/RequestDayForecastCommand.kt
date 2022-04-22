package com.component.kotlintest.app.demain.commands

import com.component.kotlintest.app.demain.datasource.ForecastProvider
import com.component.kotlintest.app.demain.model.Forecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RequestDayForecastCommand(val id: Long, private val forecastProvider: ForecastProvider = ForecastProvider()) :Command<Forecast>{

    override suspend fun execute(): Forecast = withContext(Dispatchers.IO){
        forecastProvider.requestForecast(id)
    }
}