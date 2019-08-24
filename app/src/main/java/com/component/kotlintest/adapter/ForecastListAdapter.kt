package com.component.kotlintest.adapter

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.component.kotlintest.R
import com.component.kotlintest.demain.model.Forecast
import com.component.kotlintest.extensions.toDateString
import com.squareup.picasso.Picasso


class ForecastListAdapter(
    data: MutableList<Forecast>?,
    private val itemClick: (Forecast) -> Unit
) :
    BaseQuickAdapter<Forecast, BaseViewHolder>(R.layout.item_forecast, data), BaseQuickAdapter.OnItemClickListener {

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        itemClick(adapter?.getItem(position) as Forecast)
    }

    init {
        this.onItemClickListener = this
    }

    override fun convert(helper: BaseViewHolder?, item: Forecast?) {
        if (helper != null && item != null) {
            with(item) {
                Picasso.with(mContext).load(iconUrl).into(helper.getView(R.id.icon) as ImageView)
                helper.setText(R.id.dateText, date.toDateString())
                helper.setText(R.id.descriptionText, description)
                helper.setText(R.id.maxTemperature, "${high}º")
                helper.setText(R.id.minTemperature, "${low}º")
            }
        }
    }
}