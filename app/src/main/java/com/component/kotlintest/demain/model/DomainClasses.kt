package com.component.kotlintest.demain.model

//数据类
data class ForecastList(
    val id: Long, val city: String, val country: String,
    val dailyForecast: List<Forecast>
) {
    val size: Int get() = dailyForecast.size

    //操作符重载 重载了该类的[]方法
    operator fun get(position: Int) = dailyForecast[position]
}

data class Forecast(
    val id: Long, val date: Long, val description: String, val high: Int, val low: Int,
    val iconUrl: String
)