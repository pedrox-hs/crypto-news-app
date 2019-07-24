package com.pedrenrique.cryptonews.core.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Article(
    val title: String,
    val description: String,
    @SerializedName("content")
    private val contentText: String?,
    @SerializedName("urlToImage")
    val imageUrl: String?,
    val publishedAt: Date,
    @SerializedName("author")
    private val authorText: String?,
    val source: Source,
    val url: String
) : Parcelable {
    val author: String
        get() = authorText ?: source.name

    val content: String
        get() = contentText?.replace("\\[.[0-9]+ chars\\]$".toRegex(), "") ?: description
}