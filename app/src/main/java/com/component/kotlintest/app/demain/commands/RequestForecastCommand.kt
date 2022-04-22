package com.component.kotlintest.app.demain.commands

import com.component.kotlintest.app.demain.datasource.ForecastProvider
import com.component.kotlintest.app.demain.model.ForecastList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RequestForecastCommand(
    private val zipCode: Long,
    private val forecastProvider: ForecastProvider = ForecastProvider()
) : Command<ForecastList> {


    companion object {
        const val DAYS = 7
    }

    override suspend fun execute(): ForecastList = withContext(Dispatchers.IO) {
        forecastProvider.requestByZipCode(zipCode, DAYS)
    }
}