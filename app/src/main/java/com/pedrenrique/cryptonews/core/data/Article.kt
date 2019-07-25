package com.pedrenrique.cryptonews.core.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

typealias Image = String

@Parcelize
data class Article(
    val title: String,
    val description: String,
    @SerializedName("content")
    val originalContent: String?,
    @SerializedName("urlToImage")
    val imageUrl: Image?,
    val publishedAt: Date,
    @SerializedName("author")
    val originalAuthor: String?,
    val source: Source,
    val url: String
) : Parcelable {
    val author: String
        get() = originalAuthor ?: source.name

    val content: String
        get() = originalContent?.replace("\\[.[0-9]+ chars\\]$".toRegex(), "") ?: description
}