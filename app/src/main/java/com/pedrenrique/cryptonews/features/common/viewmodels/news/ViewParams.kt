package com.pedrenrique.cryptonews.features.common.viewmodels.news

import com.pedrenrique.cryptonews.R
import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.features.common.adapter.ViewParams

class NewsViewParams(val item: Article) : ViewParams {
    override val layoutRes = R.layout.item_news
}

class LoadingViewParams : ViewParams {
    override val layoutRes = R.layout.item_loading
}

class ErrorViewParams(val error: Throwable) : ViewParams {
    override val layoutRes = R.layout.item_error
}