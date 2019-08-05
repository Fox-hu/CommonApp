package com.component.kotlintest.demain.commands

import com.component.kotlintest.demain.datasource.ForecastProvider
import com.component.kotlintest.demain.model.Forecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RequestDayForecastCommand(val id: Long, private val forecastProvider: ForecastProvider = ForecastProvider()) :Command<Forecast>{

    override suspend fun execute(): Forecast = withContext(Dispatchers.IO){
        forecastProvider.requestForecast(id)
    }
}