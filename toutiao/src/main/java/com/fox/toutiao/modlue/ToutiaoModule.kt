package com.fox.toutiao.modlue

import com.fox.toutiao.repository.NewsRepository
import com.fox.toutiao.ui.home.HomeViewModel
import com.fox.toutiao.ui.news.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel() }
    viewModel { NewsViewModel(get()) }
}

val repositoryModule = module {
    single { NewsRepository() }
}

val appModule = listOf(viewModelModule, repositoryModule)