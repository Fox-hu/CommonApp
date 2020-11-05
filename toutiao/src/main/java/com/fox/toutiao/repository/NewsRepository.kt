package com.fox.toutiao.repository

import com.fox.toutiao.R
import com.fox.toutiao.ui.news.bean.NewsChannelBean
import com.silver.fox.base.component.repository.BaseRepository
import com.silver.fox.ext.getStringArray

class NewsRepository : BaseRepository() {
    companion object {
        const val CATEGORY_LIMIT = 8
        const val NEWS_CHANNEL_ENABLE = true
        const val NEWS_CHANNEL_DISABLE = false
    }

    fun getNewsCategoryList(): List<NewsChannelBean> {
        val newsIds = R.array.mobile_news_id.getStringArray()
        val newsNames = R.array.mobile_news_name.getStringArray()
        val ret = ArrayList<NewsChannelBean>()
        for (i in 0..CATEGORY_LIMIT) {
            ret += NewsChannelBean(newsIds[i], newsNames[i], NEWS_CHANNEL_ENABLE, i)
        }
        return ret
    }
}