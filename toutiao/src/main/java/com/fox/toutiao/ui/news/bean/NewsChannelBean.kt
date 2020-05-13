package com.fox.toutiao.ui.news.bean

/**
 * @Author fox.hu
 * @Date 2020/5/13 13:53
 */

data class NewsChannelBean(
    var channelId: String,
    var channelName: String,
    var isEnable: Boolean = false,
    var position: Int = 0
) :
    Comparable<NewsChannelBean> {
    override fun compareTo(other: NewsChannelBean): Int {
        return this.position - other.position
    }
}