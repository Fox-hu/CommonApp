package com.fox.toutiao.ui.news


import android.os.Bundle
import com.fox.toutiao.R
import com.fox.toutiao.databinding.FragmentNewsArticleBinding
import com.silver.fox.base.component.ui.BaseVMFragment
import kotlinx.android.synthetic.main.fragment_news_article.*
import kotlinx.android.synthetic.main.fragment_news_article.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewsArticleFragment : BaseVMFragment<NewsArticleViewModel, FragmentNewsArticleBinding>() {

    override val viewModel: NewsArticleViewModel by viewModel()

    override fun getLayoutResId(): Int = R.layout.fragment_news_article
    override fun bindDataAndEvent() {

    }

    companion object Factory {
        operator fun invoke(categoryId: String): NewsArticleFragment {
            val bundle = Bundle()
            bundle.putString("categoryId", categoryId)
            val fragment = NewsArticleFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
