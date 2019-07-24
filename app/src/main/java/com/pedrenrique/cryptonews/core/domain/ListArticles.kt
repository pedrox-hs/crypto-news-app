package com.pedrenrique.cryptonews.core.domain

import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.data.PaginatedData
import com.pedrenrique.cryptonews.core.data.datasource.NewsDataSource

class ListArticles(
    private val dataSource: NewsDataSource
) : UseCase.NoParam<PaginatedData<Article>>() {
    override suspend fun run() =
        dataSource.list()
}