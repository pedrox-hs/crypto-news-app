package com.pedrenrique.cryptonews.features.news

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pedrenrique.cryptonews.R
import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.data.SortType
import com.pedrenrique.cryptonews.core.ext.*
import com.pedrenrique.cryptonews.core.platform.Language
import com.pedrenrique.cryptonews.core.platform.LocaleManager
import com.pedrenrique.cryptonews.core.platform.widget.RadioOptionsDialog
import com.pedrenrique.cryptonews.features.common.adapter.RecyclerViewAdapter
import com.pedrenrique.cryptonews.features.common.adapter.ViewParams
import com.pedrenrique.cryptonews.features.common.listeners.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.layout_error_state.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : Fragment(), NewsAdapter.OnItemClickListener,
    RadioOptionsDialog.OnSelectedItemListener {

    // region Class fields
    private val newsViewModel by viewModel<NewsViewModel>()
    private val adapter = NewsAdapter()

    private val RecyclerView.linearLayoutManager: LinearLayoutManager
        get() = layoutManager as LinearLayoutManager
    // endregion

    companion object {
        private const val REQUEST_CODE_CHANGE_SORT = 1
        private const val REQUEST_CODE_CHANGE_LANGUAGE = 2
    }

    // region Platform overrides
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_news, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        newsViewModel.setup()
        setupList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_sort -> {
                onSortOptionClick()
                true
            }
            R.id.action_change_language -> {
                onLanguageOptionClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    // endregion

    // region Setup
    private fun NewsViewModel.setup() {
        state.observe(this@NewsFragment, Observer {
            onNewsListChanged(it)
        })
        load()
    }

    private fun RecyclerView.setup(adapter: RecyclerViewAdapter<ViewParams>, loadMore: () -> Unit) {
        layoutManager = LinearLayoutManager(context)

        val decoration = DividerItemDecoration(context, linearLayoutManager.orientation)
        decoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider)!!)
        addItemDecoration(decoration)

        val endlessScrollListener = EndlessRecyclerViewScrollListener(linearLayoutManager, loadMore)
        addOnScrollListener(endlessScrollListener)

        this.adapter = adapter
        setHasFixedSize(true)
    }

    private fun setupList() {
        adapter.setOnItemClickListener(this)
        rvNews.setup(adapter, newsViewModel::loadMore)
        swipe.setOnRefreshListener(newsViewModel::refresh)
        btnTryAgain.setOnClickListener { newsViewModel.load() }
    }
    // endregion

    // region Listeners

    // region Data change handlers
    private fun onNewsListChanged(it: NewsListState?) {
        adapter.replace(it?.data ?: listOf())
        when (it) {
            is NewsListState.Requesting -> onRequestingNews()
            is NewsListState.Failed -> onLoadArticlesFailed(it.error)
            is NewsListState.NextFailed -> onLoadNextFailed(it.error)
            else -> onLoadDone()
        }
    }

    private fun onRequestingNews() {
        swipe.isRefreshing = true
        layoutError.gone()
    }

    private fun onLoadDone() {
        swipe.isRefreshing = false
        rvNews.show()
        layoutError.gone()
    }

    private fun onLoadNextFailed(error: Throwable) {
        swipe.isRefreshing = false
        Toast.makeText(context, error.defaultFriendlyTitle, Toast.LENGTH_LONG).show()
    }

    private fun onLoadArticlesFailed(error: Throwable) {
        swipe.isRefreshing = false
        rvNews.gone()
        layoutError.show()
        ivProblem.setImageResource(error.defaultErrorDrawable)
        tvErrorTitle.setText(error.defaultFriendlyTitle)
        tvErrorMessage.setText(error.defaultFriendlyMsg)
    }
    // endregion

    // region User event handlers
    private fun onSortOptionClick() {
        RadioOptionsDialog.create {
            requestCode = REQUEST_CODE_CHANGE_SORT
            selected = newsViewModel.sortType.ordinal
            items = SortType.values().map { getString(it.displayValue) }
            target = this@NewsFragment
        }.show(supportActivity?.navFragmentManager!!)
    }

    private fun onChangeNewsSort(which: Int) {
        when (SortType.values()[which]) {
            SortType.PUBLISHED_AT -> newsViewModel.sortByPublishDate()
            SortType.POPULARITY -> newsViewModel.sortByPopularity()
        }
    }

    private fun onLanguageOptionClick() {
        RadioOptionsDialog.create {
            requestCode = REQUEST_CODE_CHANGE_LANGUAGE
            items = Language.values().map { getString(it.displayText) }
            selected = LocaleManager.getLanguage(context!!).ordinal
            target = this@NewsFragment
        }.show(supportActivity?.navFragmentManager!!)
    }

    private fun onChangeLanguage(which: Int) {
        val language = Language.values()[which]
        if (language != LocaleManager.getLanguage(context!!)) {
            LocaleManager.setLanguage(context!!, language)
            newsViewModel.refresh()
            supportActivity?.recreate()
        }
    }

    override fun onArticleClick(article: Article) {
        val act = activity ?: return
        val navController = Navigation.findNavController(act, R.id.navHostFragment)
        val showArticle = NewsFragmentDirections.showArticle(article)
        navController.navigate(showArticle)
    }

    override fun onRetryClick() {
        newsViewModel.loadMore()
    }

    override fun onSelectedOption(requestCode: Int, which: Int) {
        when (requestCode) {
            REQUEST_CODE_CHANGE_SORT -> onChangeNewsSort(which)
            REQUEST_CODE_CHANGE_LANGUAGE -> onChangeLanguage(which)
        }
    }
    // endregion

    // endregion
}