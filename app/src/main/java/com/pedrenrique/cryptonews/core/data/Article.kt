package com.pedrenrique.cryptonews.core.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Article(
    val title: String,
    val description: String,
    val content: String,
    @SerializedName("urlToImage")
    val imageUrl: String?,
    val publishedAt: Date,
    @SerializedName("author")
    private val authorText: String?,
    val source: Source,
    val url: String
) : Parcelable {
    val author: String
        get() = when {
            authorText == null -> source.name
            source.name != "Forbes.com" -> authorText
            else -> authorText.replace("^(.+?), Contributor.+$".toRegex(), "$1")
        }
}