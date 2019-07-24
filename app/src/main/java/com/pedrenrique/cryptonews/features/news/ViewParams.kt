package com.pedrenrique.cryptonews.features.news

import com.pedrenrique.cryptonews.R
import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.features.common.adapter.ViewParams

class ArticleViewParams(val item: Article) : ViewParams {
    override val layoutRes = R.layout.item_article
}

class LoadingViewParams : ViewParams {
    override val layoutRes = R.layout.item_loading
}

class ErrorViewParams(val error: Throwable) : ViewParams {
    override val layoutRes = R.layout.item_error
}