package com.fox.toutiao.modlue

import com.fox.toutiao.repository.ArticleRepository
import com.fox.toutiao.ui.ArticleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ArticleViewModel(get()) }
}

val repositoryModule = module {
    single { ArticleRepository() }
}

val appModule = listOf(viewModelModule, repositoryModule)