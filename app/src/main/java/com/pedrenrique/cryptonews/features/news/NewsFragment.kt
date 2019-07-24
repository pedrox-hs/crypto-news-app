package com.pedrenrique.cryptonews.features.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pedrenrique.cryptonews.R
import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.ext.*
import com.pedrenrique.cryptonews.features.common.adapter.Adapter
import com.pedrenrique.cryptonews.features.common.adapter.ViewParams
import com.pedrenrique.githubapp.features.common.listeners.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.layout_error_state.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : Fragment(), ArticleAdapter.OnItemClickListener {

    private val newsViewModel by viewModel<NewsViewModel>()
    private val adapter = ArticleAdapter()

    private val RecyclerView.linearLayoutManager: LinearLayoutManager
        get() = layoutManager as LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel.setup()
        setupList()
    }

    private fun NewsViewModel.setup() {
        state.observe(this@NewsFragment, Observer {
            adapter.replace(it?.data ?: listOf())
            when (it) {
                is NewsListState.Requesting -> {
                    swipe.isRefreshing = true
                    layoutError.gone()
                }
                is NewsListState.Failed -> onLoadArticlesFailed(it.error)
                is NewsListState.NextFailed -> {
                    swipe.isRefreshing = false
                    Toast.makeText(context, it.error.defaultFriendlyTitle, Toast.LENGTH_LONG).show()
                }
                else -> {
                    swipe.isRefreshing = false
                    rvNews.show()
                    layoutError.gone()
                }
            }
        })
        load()
    }

    private fun onLoadArticlesFailed(error: Throwable) {
        swipe.isRefreshing = false
        rvNews.gone()
        layoutError.show()
        ivProblem.setImageResource(error.defaultErrorDrawable)
        tvErrorTitle.setText(error.defaultFriendlyTitle)
        tvErrorMessage.setText(error.defaultFriendlyMsg)
    }

    private fun setupList() {
        adapter.setOnItemClickListener(this)
        rvNews.setup(adapter) {
            newsViewModel.loadMore()
        }
        swipe.setOnRefreshListener {
            newsViewModel.refresh()
        }
        btnTryAgain.setOnClickListener {
            newsViewModel.load()
        }
    }

    private fun RecyclerView.setup(adapter: Adapter<ViewParams>, loadMore: () -> Unit) {
        layoutManager = LinearLayoutManager(context)
        val endlessRecyclerViewScrollListener =
            EndlessRecyclerViewScrollListener(
                linearLayoutManager,
                loadMore
            )

        val decoration = DividerItemDecoration(context, linearLayoutManager.orientation)
        decoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider)!!)
        addItemDecoration(decoration)

        this.adapter = adapter
        setHasFixedSize(true)
        addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    override fun onClick(article: Article) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRetryClick() {
        newsViewModel.loadMore()
    }
}