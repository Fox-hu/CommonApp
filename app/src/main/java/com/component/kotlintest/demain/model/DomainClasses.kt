package com.component.kotlintest.demain.model

data class ForecastList(
    val id: Long, val city: String, val country: String,
    val dailyForecast: List<Forecast>
) {
    val size: Int get() = dailyForecast.size

}

data class Forecast(
    val id: Long, val date: Long, val description: String, val high: Int, val low: Int,
    val iconUrl: String
)