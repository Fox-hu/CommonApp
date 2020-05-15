package com.fox.toutiao.ui.news

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fox.toutiao.R
import com.fox.toutiao.databinding.FragmentNewsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.silver.fox.base.component.ui.BaseVMFragment
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseVMFragment<NewsViewModel, FragmentNewsBinding>() {

    override val viewModel: NewsViewModel by viewModel()

    override fun getLayoutResId(): Int = R.layout.fragment_news

    override fun bindDataAndEvent() {
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int) = viewModel.fragmentList[position]
            override fun getItemCount() = viewModel.titleList.size
        }
        viewPager.offscreenPageLimit = 15
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewModel.titleList[position]
        }.attach()
    }

}