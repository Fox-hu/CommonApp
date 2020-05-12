package com.fox.toutiao.ui.news


import com.fox.toutiao.R
import com.fox.toutiao.databinding.FragmentNewsArticleBinding
import com.silver.fox.base.BaseVMFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewsArticleFragment : BaseVMFragment<NewsArticleViewModel, FragmentNewsArticleBinding>() {

//    val safeArgs: NewsArticleFragmentArgs by navArgs()
//    val flowStepNumber = safeArgs.categoryId

    override fun getLayoutResId(): Int = R.layout.fragment_news_article
    override fun bindDataAndEvent() {
        val flowStepNumber = arguments?.getInt("categoryId")
    }

    override val viewModel: NewsArticleViewModel by viewModel()
}
