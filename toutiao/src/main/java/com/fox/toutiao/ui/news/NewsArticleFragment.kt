package com.fox.toutiao.ui.news


import androidx.navigation.fragment.navArgs
import com.fox.toutiao.R
import com.silver.fox.base.BaseVMFragment


class NewsArticleFragment : BaseVMFragment<NewsArticleViewModel>() {

//    val safeArgs: NewsArticleFragmentArgs by navArgs()
//    val flowStepNumber = safeArgs.categoryId

    override fun startObserve() {
    }

    override fun initView() {
    }

    override fun initData() {
        val flowStepNumber = arguments?.getInt("categoryId")
    }

    override fun getLayoutResId(): Int = R.layout.fragment_news_article
}
