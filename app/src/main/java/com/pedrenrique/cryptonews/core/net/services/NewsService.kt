package com.pedrenrique.cryptonews.core.net.services

import com.pedrenrique.cryptonews.core.net.NewsResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface NewsService {
    @GET("everything")
    fun getNews(@QueryMap params: Map<String, String>): Deferred<NewsResponse>
}