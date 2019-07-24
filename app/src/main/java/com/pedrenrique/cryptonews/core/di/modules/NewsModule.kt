package com.pedrenrique.cryptonews.core.di.modules

import com.pedrenrique.cryptonews.core.data.datasource.NewsDataSource
import com.pedrenrique.cryptonews.core.domain.ListArticles
import com.pedrenrique.cryptonews.core.domain.LoadMoreArticles
import com.pedrenrique.cryptonews.core.net.services.NewsService
import com.pedrenrique.cryptonews.features.news.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val newsModule = module {
    viewModel {
        NewsViewModel(get(), get())
    }

    factory {
        ListArticles(get())
    }

    factory {
        LoadMoreArticles(get())
    }

    factory<NewsDataSource> {
        NewsDataSource.Impl(get())
    }

    factory<NewsService> {
        get<Retrofit>().create(NewsService::class.java)
    }
}