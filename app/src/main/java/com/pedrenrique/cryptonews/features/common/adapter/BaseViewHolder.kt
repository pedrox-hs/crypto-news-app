package com.pedrenrique.cryptonews.features.common.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(data: T)

    open class Generic<T>(view: View) : BaseViewHolder<T>(view) {
        override fun bind(data: T) {
            // nothing
        }
    }
}