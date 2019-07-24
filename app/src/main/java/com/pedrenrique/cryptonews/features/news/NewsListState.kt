package com.pedrenrique.cryptonews.features.news

import com.pedrenrique.cryptonews.core.ext.append
import com.pedrenrique.cryptonews.features.common.adapter.ViewParams

sealed class NewsListState(val data: List<ViewParams>) {
    constructor(vararg data: ViewParams) : this(data.toList())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewsListState

        if (data != other.data) return false

        return true
    }

    override fun hashCode() = data.hashCode()

    object Empty : NewsListState()

    object Requesting : NewsListState()
    data class RequestingNext(
            val lastData: List<ViewParams>
    ) : NewsListState(lastData.append(LoadingViewParams()))

    data class Failed(val error: Throwable) : NewsListState()
    data class NextFailed(
            val error: Throwable,
            val lastData: List<ViewParams>
    ) : NewsListState(lastData.append(ErrorViewParams(error)))

    class Loaded(data: List<ViewParams>) : NewsListState(data)
    class Completed(data: List<ViewParams>) : NewsListState(data)
}