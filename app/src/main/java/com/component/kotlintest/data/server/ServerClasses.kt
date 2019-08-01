package com.component.kotlintest.data.server

import com.component.kotlintest.demain.model.Forecast


data class ForecastResult(val city: City, val list: List<Forecast>)
data class City(val id: Long, val name: String, val coord: Coordinates, val country: String, val population: Int)
data class Coordinates(val lon: Float, val lat: Float)
data class Temperature(
    val day: Float,
    val min: Float,
    val max: Float,
    val night: Float,
    val eve: Float,
    val morn: Float
)
data class Weather(val id: Long, val main: String, val description: String, val icon: String)