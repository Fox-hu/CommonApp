package com.fox.toutiao.ui.news


import android.os.Bundle
import com.fox.toutiao.R
import com.fox.toutiao.databinding.FragmentNewsArticleBinding
import com.silver.fox.base.component.ui.BaseVMFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewsArticleFragment : BaseVMFragment<NewsArticleViewModel, FragmentNewsArticleBinding>() {

    override val viewModel: NewsArticleViewModel by viewModel()

    //    val safeArgs: NewsArticleFragmentArgs by navArgs()
//    val flowStepNumber = safeArgs.categoryId
    var categoryId: String = ""

    override fun getLayoutResId(): Int = R.layout.fragment_news_article
    override fun bindDataAndEvent() {
//        val flowStepNumber = arguments?.getInt("categoryId")
        categoryId = arguments?.getString("categoryId") ?: ""
        viewModel.categoryId = categoryId
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
