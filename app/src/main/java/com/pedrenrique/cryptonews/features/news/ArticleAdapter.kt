package com.pedrenrique.cryptonews.features.news

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.pedrenrique.cryptonews.R
import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.ext.gone
import com.pedrenrique.cryptonews.core.ext.resetTime
import com.pedrenrique.cryptonews.core.ext.setRemoteImage
import com.pedrenrique.cryptonews.core.ext.show
import com.pedrenrique.cryptonews.features.common.adapter.Adapter
import com.pedrenrique.cryptonews.features.common.adapter.BaseViewHolder
import com.pedrenrique.cryptonews.features.common.adapter.ViewParams
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.item_error.view.*
import java.util.*

class ArticleAdapter : Adapter<ViewParams>() {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    @Suppress("UNCHECKED_CAST")
    override fun getViewHolderForLayout(layout: Int, view: View) =
        when (layout) {
            R.layout.item_article -> ArticleViewHolder(view)
            R.layout.item_loading -> LoadingViewHolder(view)
            R.layout.item_error -> ErrorViewHolder(view)
            else -> throw RuntimeException("Invalid layout")
        }

    override fun getLayoutForPosition(position: Int) =
        items[position].layoutRes

    inner class ArticleViewHolder(view: View) : BaseViewHolder<ArticleViewParams>(view) {
        private val now = Calendar.getInstance().resetTime().timeInMillis

        override fun bind(data: ArticleViewParams) {
            itemView.setOnClickListener {
                onItemClickListener?.onClick(data.item)
            }
            itemView.populate(data.item)
        }

        private fun View.populate(article: Article) {
            tvArticleTitle.text = article.title
            tvArticleDescription.text = article.description
            tvArticleSource.text = article.source.name
            if (article.imageUrl != null) {
                ivArticleImage.setRemoteImage(article.imageUrl) {
                    placeholder(R.drawable.placeholder_article_image)
                    error(R.drawable.placeholder_article_image_error)
                }
                ivArticleImage.show()
            } else {
                ivArticleImage.gone()
            }
            tvPublishInfo.text = article.formatInfo(context)
        }

        private fun Article.formatInfo(context: Context): String {
            val publishedAt = Calendar.getInstance().apply {
                time = publishedAt
            }.resetTime()
            val days = (now - publishedAt.timeInMillis).toInt() / (24 * 60 * 60 * 1000)
            return when (days) {
                0 -> context.getString(R.string.text_publish_info_today, author)
                1 -> context.getString(R.string.text_publish_info_yesterday, author)
                in 2..7 -> context.getString(
                    R.string.text_publish_info_few_days,
                    author,
                    days
                )
                else -> context.getString(
                    R.string.text_publish_info_many_days,
                    author,
                    publishedAt.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()),
                    publishedAt.get(Calendar.DAY_OF_MONTH)
                )
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

    interface OnItemClickListener {
        fun onClick(article: Article)
        fun onRetryClick()
    }
}