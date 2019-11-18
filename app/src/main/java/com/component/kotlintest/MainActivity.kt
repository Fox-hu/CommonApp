package com.component.kotlintest

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.component.kotlintest.adapter.ForecastListAdapter
import com.component.kotlintest.demain.commands.RequestForecastCommand
import com.component.kotlintest.extensions.DelegatesExt
import com.component.kotlintest.ui.activity.CoroutineScopeActivity
import com.component.kotlintest.ui.activity.DetailActivity
import com.component.kotlintest.ui.activity.SettingActivity
import com.component.kotlintest.ui.activity.ToolbarManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

class MainActivity : CoroutineScopeActivity(), ToolbarManager {

    override val toolbar by lazy { find<android.support.v7.widget.Toolbar>(R.id.toolbar) }
    private val zipCode: Long by DelegatesExt.preference(
        this, SettingActivity.ZIP_CODE,
        SettingActivity.DEFAULT_ZIP

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()

        forecastList.layoutManager = LinearLayoutManager(this)
        attachToScroll(forecastList)
    }

    override fun onResume() {
        super.onResume()
        loadForecast()
    }

    private fun loadForecast() = launch {
        val result = RequestForecastCommand(zipCode).execute()
        val forecastListAdapter = ForecastListAdapter(result.dailyForecast.toMutableList()) {
            startActivity<DetailActivity>(DetailActivity.ID to it.id, DetailActivity.CITY_NAME to result.city)
        }
        forecastList.adapter = forecastListAdapter
        toolbarTitle = "${result.city} (${result.country})"
    }
}
