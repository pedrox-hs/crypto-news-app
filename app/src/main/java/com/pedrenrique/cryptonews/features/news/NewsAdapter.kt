package com.pedrenrique.cryptonews.features.news

import android.view.View
import com.pedrenrique.cryptonews.R
import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.ext.*
import com.pedrenrique.cryptonews.features.common.adapter.RecyclerViewAdapter
import com.pedrenrique.cryptonews.features.common.adapter.BaseViewHolder
import com.pedrenrique.cryptonews.features.common.adapter.ViewParams
import com.pedrenrique.cryptonews.features.common.viewmodels.news.ErrorViewParams
import com.pedrenrique.cryptonews.features.common.viewmodels.news.LoadingViewParams
import com.pedrenrique.cryptonews.features.common.viewmodels.news.NewsViewParams
import kotlinx.android.synthetic.main.item_error.view.*
import kotlinx.android.synthetic.main.item_news.view.*
import java.util.*

class NewsAdapter : RecyclerViewAdapter<ViewParams>() {

    private var onItemClickListener: OnItemClickListener? = null

    @Suppress("UNCHECKED_CAST")
    override fun getViewHolderForLayout(layout: Int, view: View) =
        when (layout) {
            R.layout.item_news -> NewsViewHolder(view)
            R.layout.item_loading -> LoadingViewHolder(view)
            R.layout.item_error -> ErrorViewHolder(view)
            else -> throw RuntimeException("Invalid layout")
        }

    override fun getLayoutForPosition(position: Int) =
        getItemForPosition(position).layoutRes

    // region Listeners
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onArticleClick(article: Article)
        fun onRetryClick()
    }
    // endregion

    // region ViewHolders
    inner class NewsViewHolder(view: View) : BaseViewHolder<NewsViewParams>(view) {
        private val now = Calendar.getInstance()

        override fun bind(data: NewsViewParams) {
            itemView.setOnClickListener {
                onItemClickListener?.onArticleClick(data.item)
            }
            itemView.populate(data.item)
        }

        private fun View.populate(article: Article) {
            tvNewsTitle.text = article.title
            tvNewsDescription.text = article.description
            tvNewsSource.text = article.source.name
            tvNewsAuthor.text = article.author
            tvNewsPublishedAt.text = article.publishedAt.formatElapsedDays(context, now)
            if (article.imageUrl != null) {
                ivNewsImage.setRemoteImage(article.imageUrl) {
                    placeholder(R.drawable.placeholder_article_image)
                    error(R.drawable.placeholder_article_image_error)
                }
                ivNewsImage.show()
            } else {
                ivNewsImage.gone()
            }
        }
    }

    inner class ErrorViewHolder(view: View) : BaseViewHolder<ErrorViewParams>(view) {
        override fun bind(data: ErrorViewParams) {
            itemView.btnLoadMore.setOnClickListener {
                onItemClickListener?.onRetryClick()
            }
        }
    }

    class LoadingViewHolder(view: View) : BaseViewHolder<LoadingViewParams>(view) {
        override fun bind(data: LoadingViewParams) {
            // nothing
        }
    }
    // endregion
}