package com.pedrenrique.cryptonews.core.net

import com.pedrenrique.cryptonews.core.data.Article

data class NewsResponse(
    val articles: List<Article>
)