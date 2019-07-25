package com.pedrenrique.cryptonews.features.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.pedrenrique.cryptonews.R
import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.data.Image
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
        setTitle(args.article.source.name.toLowerCase(), getString(R.string.subtitle_article))
    }

    private fun Article.populate() {
        tvArticleTitle.text = title
        tvArticleContent.text = content
        tvArticleAuthor.text = getString(R.string.text_author, author)
        tvArticlePublishedAt.text = publishedAt.formatElapsedDays(context!!, Calendar.getInstance())
        imageUrl?.load() ?: ivArticleImage.gone()
    }

    private fun Image.load() {
        ivArticleImage.setRemoteImage(this) {
            placeholder(R.drawable.placeholder_article_image)
            error(R.drawable.placeholder_article_image_error)
        }
        ivArticleImage.show()
    }

    private fun bindEvents() {
        btnReadMore.setOnClickListener(::onReadMoreClick)
    }

    private fun onReadMoreClick(view: View) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(args.article.url)
        val packageManager = activity!!.packageManager
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i)
        } else {
            Toast.makeText(context, R.string.msg_cant_open_article, Toast.LENGTH_LONG).show()
        }
    }
}
