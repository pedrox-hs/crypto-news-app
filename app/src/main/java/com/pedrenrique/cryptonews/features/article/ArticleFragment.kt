package com.pedrenrique.cryptonews.features.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.pedrenrique.cryptonews.R
import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.ext.*
import kotlinx.android.synthetic.main.fragment_article.*
import java.util.*

class ArticleFragment : Fragment() {

    private val args by navArgs<ArticleFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_article, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportActionBar?.setup()
        args.article.populate()
        bindEvents()
    }

    private fun ActionBar.setup() {
        setDisplayHomeAsUpEnabled(true)
        setTitle(args.article.source.name.toUpperCase(), getString(R.string.subtitle_article))
    }

    private fun Article.populate() {
        tvArticleTitle.text = title
        tvArticleContent.text = content
        tvArticleAuthor.text = getString(R.string.text_author, author)
        tvArticlePublishedAt.text = publishedAt.formatElapsedDays(context!!, Calendar.getInstance())
        if (imageUrl != null) {
            ivArticleImage.setRemoteImage(imageUrl) {
                placeholder(R.drawable.placeholder_article_image)
                error(R.drawable.placeholder_article_image_error)
            }
            ivArticleImage.show()
        } else {
            ivArticleImage.gone()
        }
    }

    private fun bindEvents() {
        btnReadMore.setOnClickListener {
            // todo
        }
    }
}
