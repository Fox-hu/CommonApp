package com.fox.toutiao.ui.news

import com.fox.toutiao.R
import com.fox.toutiao.databinding.FragmentNewsBinding
import com.silver.fox.base.BaseVMFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseVMFragment<NewsViewModel, FragmentNewsBinding>() {

    override fun getLayoutResId(): Int = R.layout.fragment_news
    override fun bindDataAndEvent() {
    }

    override val viewModel: NewsViewModel by viewModel()
}