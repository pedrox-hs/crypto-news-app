package com.pedrenrique.cryptonews.core.ext

fun <T> List<T>.append(vararg items: T): List<T> {
    return toMutableList().apply {
        addAll(items)
    }
}