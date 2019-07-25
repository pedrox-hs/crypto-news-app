package com.pedrenrique.cryptonews.core.data.datasource

import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.data.PaginatedData
import com.pedrenrique.cryptonews.core.data.SortType
import com.pedrenrique.cryptonews.core.exceptions.EmptyResultException
import com.pedrenrique.cryptonews.core.exceptions.NoMoreResultException
import com.pedrenrique.cryptonews.core.ext.resetTime
import com.pedrenrique.cryptonews.core.net.services.NewsService
import java.text.SimpleDateFormat
import java.util.*

interface NewsDataSource {

    suspend fun list(sort: SortType): PaginatedData<Article>

    suspend fun loadMore(lastPage: Int, sort: SortType): PaginatedData<Article>

    class Impl(private val service: NewsService) : NewsDataSource {
        override suspend fun list(sort: SortType) = loadPage(1, sort)

        override suspend fun loadMore(lastPage: Int, sort: SortType) =
            try {
                loadPage(lastPage + 1, sort)
            } catch (e: EmptyResultException) {
                throw NoMoreResultException()
            }

        private suspend fun loadPage(page: Int, sort: SortType): PaginatedData<Article> {
            val tz = TimeZone.getTimeZone("UTC")

            val fromDate = Calendar.getInstance(tz).resetTime()
            fromDate.add(Calendar.DAY_OF_YEAR, -20)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateFormat.timeZone = tz

            val language = Locale.getDefault().language

            val response = service.getNews(
                mapOf(
                    "q" to "cryptocurrency OR criptomoeda",
                    "from" to dateFormat.format(fromDate.time),
                    "pageSize" to "20",
                    "page" to page.toString(),
                    "language" to language,
                    "sortBy" to sort.value
                )
            ).await()

            if (response.articles.isEmpty())
                throw EmptyResultException()

            return PaginatedData(page, response.articles)
        }
    }
}