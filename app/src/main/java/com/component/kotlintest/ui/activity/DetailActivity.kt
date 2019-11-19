package com.component.kotlintest.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.component.kotlintest.R
import com.component.kotlintest.demain.commands.RequestDayForecastCommand
import com.component.kotlintest.demain.model.Forecast
import com.component.kotlintest.extensions.color
import com.component.kotlintest.extensions.textColor
import com.component.kotlintest.extensions.toDateString
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.find
import java.text.DateFormat

class DetailActivity : CoroutineScopeActivity(), ToolbarManager {

    companion object {
        const val ID = "DetailActivity:id"
        const val CITY_NAME = "DetailActivity:cityName"
    }

    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initToolbar()

        toolbarTitle = intent.getStringExtra(CITY_NAME)
        enableHomeAsUp { onBackPressed() }

        launch {
            val id = intent.getLongExtra(ID, -1)
            val result = RequestDayForecastCommand(id).execute()
            bindForecast(result)
        }
    }

    private fun bindForecast(result: Forecast) {
        with(result) {
            Picasso.with(this@DetailActivity).load(iconUrl).into(icon)
            toolbar.subtitle = date.toDateString(DateFormat.FULL)
            weatherDescription.text = description
            bindWeather(high to maxTemperature, low to minTemperature)
        }
    }

    private fun bindWeather(vararg views: Pair<Int, TextView>) = views.forEach {
        it.second.text = "${it.first}"
        it.second.textColor = color(
            when (it.first) {
                in -50..0 -> android.R.color.holo_red_dark
                in 0..15 -> android.R.color.holo_orange_dark
                else -> android.R.color.holo_green_dark
            }
        )
    }
}