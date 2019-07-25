package com.pedrenrique.cryptonews.features.news

import com.pedrenrique.cryptonews.core.ext.append
import com.pedrenrique.cryptonews.features.common.adapter.ViewParams

/**
 * Class to represent states of news list
 *
 * @param data List of #ViewParams
 */
sealed class NewsListState(val data: List<ViewParams>) {
    constructor(vararg data: ViewParams) : this(data.toList())

    /**
     * Object state to indicate when no have results to receive
     */
    object Empty : NewsListState()

    /**
     * Object state to indicate first data request
     */
    object Requesting : NewsListState()

    /**
     * Class state to indicate next page request
     *
     * @param lastData list of NewsViewParams
     */
    data class RequestingNext(
        val lastData: List<ViewParams>
    ) : NewsListState(lastData.append(LoadingViewParams()))

    /**
     * Class state to indicate first data request failed
     * @param error Throwable
     */
    data class Failed(val error: Throwable) : NewsListState()

    /**
     * Class state to indicate next page request failed
     *
     * @param error Throwable
     * @param lastData list of NewsViewParams
     */
    data class NextFailed(
        val error: Throwable,
        val lastData: List<ViewParams>
    ) : NewsListState(lastData.append(ErrorViewParams(error)))

    /**
     * Class state to indicate when data is loaded successfully
     *
     * @param data list of NewsViewParams
     */
    class Loaded(data: List<ViewParams>) : NewsListState(data)

    /**
     * Class state to indicate when all data is loaded and no have more results
     *
     * @param data list of NewsViewParams
     */
    class Completed(data: List<ViewParams>) : NewsListState(data)

    // region Equals and hashcode
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewsListState

        if (data != other.data) return false

        return true
    }

    override fun hashCode() = data.hashCode()
    // endregion
}