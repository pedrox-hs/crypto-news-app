package com.pedrenrique.cryptonews.core.domain

import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.data.PaginatedData
import com.pedrenrique.cryptonews.core.data.datasource.NewsDataSource

class LoadMoreArticles(
    private val dataSource: NewsDataSource
) : UseCase<LoadMoreArticles.Params, PaginatedData<Article>>() {

    override suspend fun run(params: Params) =
        dataSource.loadMore(params.lastPage)

    data class Params(val lastPage: Int)
}