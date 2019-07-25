package com.pedrenrique.cryptonews.features.common.viewmodels.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.data.PaginatedData
import com.pedrenrique.cryptonews.core.data.SortingMode
import com.pedrenrique.cryptonews.core.domain.ListArticles
import com.pedrenrique.cryptonews.core.domain.LoadMoreArticles
import com.pedrenrique.cryptonews.core.exceptions.EmptyResultException
import com.pedrenrique.cryptonews.core.exceptions.NoMoreResultException
import com.pedrenrique.cryptonews.features.common.adapter.ViewParams
import kotlinx.coroutines.launch

class NewsViewModel(
        private val listArticles: ListArticles,
        private val loadMoreArticles: LoadMoreArticles
) : ViewModel() {

    // region ViewModel fields
    private var page = 0

    val state = MutableLiveData<NewsListState>()

    var actualSortingMode = SortingMode.PUBLISHED_AT.ordinal
        set(value) {
            if (value != field && value >= 0 && value < availableSortingOptions.size) {
                field = value
                refresh()
            }
        }

    private var sortingMode: SortingMode
        get() = availableSortingOptions[actualSortingMode]
        set(value) {
            actualSortingMode = value.ordinal
        }

    val availableSortingOptions = SortingMode.values().asList()
    // endregion

    // region Load data
    fun load() {
        val value = state.value
        if (value == null || value is NewsListState.Failed) {
            state.value =
                NewsListState.Requesting
            retrieveData {
                listArticles(ListArticles.Params(sortingMode))
            }
        }
    }

    fun loadMore() {
        val value = state.value
        val data = (value as? NewsListState.Loaded)?.data ?: (value as? NewsListState.NextFailed)?.lastData
        if (data != null) {
            state.value =
                NewsListState.RequestingNext(
                    data
                )
            retrieveData(data) {
                loadMoreArticles(LoadMoreArticles.Params(page, sortingMode))
            }
        }
    }

    fun refresh() {
        state.value =
            NewsListState.Requesting
        retrieveData {
            listArticles(ListArticles.Params(sortingMode))
        }
    }
    // endregion

    // region Request sorted data
    fun setSortingMode(mode: Int) {
        actualSortingMode = mode
    }
    // endregion

    // region Requesting data
    private fun retrieveData(
            lastData: List<ViewParams>? = null,
            provider: suspend () -> PaginatedData<Article>
    ) {
        viewModelScope.launch {
            state.value = try {
                onDataReceived(lastData, provider())
            } catch (e: EmptyResultException) {
                NewsListState.Empty
            } catch (e: NoMoreResultException) {
                NewsListState.Completed(
                    lastData ?: listOf()
                )
            } catch (e: Throwable) {
                lastData?.let {
                    NewsListState.NextFailed(
                        e,
                        it
                    )
                }
                        ?: NewsListState.Failed(
                            e
                        )
            }
        }
    }

    private fun onDataReceived(
            lastData: List<ViewParams>?,
            result: PaginatedData<Article>
    ): NewsListState.Loaded {
        val data = (lastData ?: listOf()).toMutableSet()
        data.addAll(result.content.map {
            NewsViewParams(it)
        })
        page = result.page
        return NewsListState.Loaded(data.toList())
    }
    // endregion

}