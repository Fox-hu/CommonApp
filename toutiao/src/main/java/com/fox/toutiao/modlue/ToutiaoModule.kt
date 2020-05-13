package com.fox.toutiao.modlue

import com.fox.toutiao.repository.NewsArticleRepository
import com.fox.toutiao.repository.NewsRepository
import com.fox.toutiao.repository.PhotoRepository
import com.fox.toutiao.ui.home.HomeViewModel
import com.fox.toutiao.ui.news.NewsViewModel
import com.fox.toutiao.ui.media.MediaViewModel
import com.fox.toutiao.ui.news.NewsArticleViewModel
import com.fox.toutiao.ui.photo.PhotoViewModel
import com.fox.toutiao.ui.setting.SettingViewModel
import com.fox.toutiao.ui.video.VideoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel() }
    viewModel { NewsViewModel(get()) }
    viewModel { NewsArticleViewModel(get()) }
    viewModel { MediaViewModel() }
    viewModel { PhotoViewModel(get()) }
    viewModel { SettingViewModel() }
    viewModel { VideoViewModel() }
}

val repositoryModule = module {
    single { NewsRepository() }
    single { NewsArticleRepository() }
    single { PhotoRepository() }
}

val appModule = listOf(viewModelModule, repositoryModule)