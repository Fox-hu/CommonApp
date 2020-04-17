package com.fox.toutiao.ui.news


import com.fox.framework.BaseVMFragment
import com.fox.toutiao.R


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
