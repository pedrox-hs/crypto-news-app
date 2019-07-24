package com.pedrenrique.cryptonews.features.common.adapter

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class Adapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {

    var items: List<T> = emptyList()
        private set

    private var dataVersion = 0

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, layout: Int): BaseViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(layout, parent, false)
        return getViewHolderForLayout(layout, view) as BaseViewHolder<T>
    }

    abstract fun getViewHolderForLayout(layout: Int, view: View): BaseViewHolder<*>

    abstract fun getLayoutForPosition(position: Int): Int

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(getItemForPosition(position))
    }

    override fun getItemViewType(position: Int) =
        getLayoutForPosition(position)

    override fun getItemCount() =
        items.size

    @MainThread
    fun replace(update: List<T>) {
        dataVersion++
        when {
            items.isEmpty() -> {
                if (update.isEmpty()) return
                items = update
                notifyDataSetChanged()
            }
            update.isEmpty() -> {
                val oldSize = items.size
                items = listOf()
                notifyItemRangeRemoved(0, oldSize)
            }
            else -> Task(dataVersion, items, update).execute()
        }
    }

    private fun getItemForPosition(position: Int) =
        items[position]

    open fun areItemsTheSame(oldItem: T?, newItem: T?) =
        oldItem == newItem

    open fun areContentsTheSame(oldItem: T?, newItem: T?) =
        oldItem == newItem

    @SuppressLint("StaticFieldLeak")
    inner class Task(
        private val startVersion: Int,
        private val oldItems: List<T>,
        private val newItems: List<T>
    ) : AsyncTask<Void, Void, DiffUtil.DiffResult>() {

        override fun doInBackground(vararg voids: Void?): DiffUtil.DiffResult =
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = oldItems.size

                override fun getNewListSize() = newItems.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    this@Adapter.areItemsTheSame(
                        oldItems[oldItemPosition],
                        newItems[newItemPosition]
                    )

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    this@Adapter.areContentsTheSame(
                        oldItems[oldItemPosition],
                        newItems[newItemPosition]
                    )
            })

        override fun onPostExecute(result: DiffUtil.DiffResult) {
            if (startVersion == dataVersion) {
                items = newItems
                result.dispatchUpdatesTo(this@Adapter)
            }
        }
    }
}