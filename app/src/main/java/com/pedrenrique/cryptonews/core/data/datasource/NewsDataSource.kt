package com.pedrenrique.cryptonews.core.data.datasource

import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.data.PaginatedData
import com.pedrenrique.cryptonews.core.exceptions.EmptyResultException
import com.pedrenrique.cryptonews.core.exceptions.NoMoreResultException
import com.pedrenrique.cryptonews.core.ext.resetTime
import com.pedrenrique.cryptonews.core.net.services.NewsService
import java.text.SimpleDateFormat
import java.util.*

interface NewsDataSource {

    suspend fun list(): PaginatedData<Article>

    suspend fun loadMore(lastPage: Int): PaginatedData<Article>

    class Impl(private val service: NewsService) : NewsDataSource {
        override suspend fun list() = loadPage(1)

        override suspend fun loadMore(lastPage: Int) =
            try {
                loadPage(lastPage + 1)
            } catch (e: EmptyResultException) {
                throw NoMoreResultException()
            }

        private suspend fun loadPage(page: Int): PaginatedData<Article> {
            val tz = TimeZone.getTimeZone("UTC")
            val now = Calendar.getInstance(tz).resetTime()
            now.add(Calendar.DAY_OF_YEAR, -20)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateFormat.timeZone = tz
            val language = "pt"//Locale.getDefault().language.takeIf { it in listOf("en", "pt") } ?: "en"
            val response = service.getNews(
                mapOf(
                    "q" to "cryptocurrency OR criptomoeda",
                    "from" to dateFormat.format(now.time),
                    "pageSize" to "20",
                    "page" to page.toString(),
                    "language" to language,
                    "sortBy" to "popularity"
                )
            ).await()

            if (response.articles.isEmpty())
                throw EmptyResultException()

            return PaginatedData(page, response.articles)
        }
    }
}