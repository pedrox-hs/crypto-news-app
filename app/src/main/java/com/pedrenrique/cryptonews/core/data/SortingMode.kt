package com.pedrenrique.cryptonews.core.data

import androidx.annotation.StringRes
import com.pedrenrique.cryptonews.R

enum class SortingMode(
    val value: String,
    @StringRes
    val displayValue: Int
) {
    PUBLISHED_AT("publishedAt", R.string.sort_by_publish_date),
    POPULARITY("popularity", R.string.sort_by_popularity)
}