package com.pedrenrique.cryptonews.core.domain

import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.data.PaginatedData
import com.pedrenrique.cryptonews.core.data.SortingMode
import com.pedrenrique.cryptonews.core.data.datasource.NewsDataSource

class ListArticles(
    private val dataSource: NewsDataSource
) : UseCase<ListArticles.Params, PaginatedData<Article>>() {
    override suspend fun run(params: Params) =
        dataSource.list(params.sortBy)

    data class Params(val sortBy: SortingMode)
}