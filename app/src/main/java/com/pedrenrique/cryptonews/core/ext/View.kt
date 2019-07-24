package com.pedrenrique.cryptonews.core.ext

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions

fun ImageView.setRemoteImage(url: String, block: RequestBuilder<Drawable>.() -> Unit = {}) {
    Glide.with(context)
            .load(url)
//            .apply(RequestOptions.centerCropTransform())
            .apply { block(this@apply) }
            .into(this)
}

fun View.gone() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun ActionBar.setTitle(title: CharSequence?, subtitle: CharSequence?) {
    this.title = subtitle
    this.subtitle = title
}