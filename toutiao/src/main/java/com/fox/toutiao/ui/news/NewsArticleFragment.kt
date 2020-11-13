package com.fox.toutiao.ui.news


import android.os.Bundle
import androidx.lifecycle.Observer
import com.fox.network.request.OriResult
import com.fox.toutiao.R
import com.fox.toutiao.databinding.FragmentNewsArticleBinding
import com.silver.fox.base.component.ui.BaseVMFragment
import com.silver.fox.ext.logi
import kotlinx.android.synthetic.main.fragment_news_article.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewsArticleFragment : BaseVMFragment<NewsArticleViewModel, FragmentNewsArticleBinding>() {

    override val viewModel: NewsArticleViewModel by viewModel()

    override fun getLayoutResId(): Int = R.layout.fragment_news_article

    override fun bindDataAndEvent() {
//        viewModel.pageStatus.observe(viewLifecycleOwner, Observer {
//            "status = ${it.name}".logi("DataBindingRefreshLayout")
//            when (it) {
//                OriResult.Status.LOADING -> refresh_layout.autoRefresh()
//                else -> refresh_layout.stopRefresh()
//            }
//        })
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
