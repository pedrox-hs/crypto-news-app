package com.pedrenrique.cryptonews.core.platform.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.pedrenrique.cryptonews.R


class RadioOptionsDialog : DialogFragment(), DialogInterface.OnClickListener {

    companion object {
        private val TAG: String = RadioOptionsDialog::class.java.name

        const val EXTRA_SELECTED = "EXTRA_SELECTED"
        const val EXTRA_ITEMS = "EXTRA_ITEMS"
        const val EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE"

        private const val REQUEST_CODE = 1

        fun create(block: Builder.() -> Unit) = Builder().apply(block).build()

        fun get(fragmentManager: FragmentManager): DialogFragment? =
            fragmentManager.findFragmentByTag(TAG) as? DialogFragment
    }

    private val adapterItems: ArrayList<String>
        get() = arguments?.getStringArrayList(EXTRA_ITEMS) ?: arrayListOf()

    private val selectedItem: Int
        get() = arguments?.getInt(EXTRA_SELECTED, 0) ?: 0

    private val requestCode: Int
        get() = arguments?.getInt(EXTRA_REQUEST_CODE, REQUEST_CODE) ?: REQUEST_CODE

    private val adapter by lazy {
        RadioOptionsAdapter(
            context!!,
            adapterItems,
            selectedItem
        )
    }

    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context!!)
            .setTitle(R.string.menu_sort_by)
            .setAdapter(adapter, null)
            .setPositiveButton(android.R.string.ok, this)
            .setNegativeButton(android.R.string.cancel, this)
            .create()

    override fun onClick(dialog: DialogInterface?, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            setResult(adapter.selected)
        }
    }

    private fun setResult(selected: Int) {
        val listener = (targetFragment ?: activity) as? OnSelectedItemListener
        listener?.onSelectedOption(requestCode, selected) ?: Log.e(
            TAG,
            "Unable to send result, implement `OnSelectedItemListener` or set `target`"
        )
        dismiss()
    }

    interface OnSelectedItemListener {
        fun onSelectedOption(requestCode: Int, which: Int)
    }

    private class RadioOptionsAdapter(
        context: Context,
        items: List<String>,
        var selected: Int = 0
    ) :
        ArrayAdapter<String>(
            context,
            android.R.layout.simple_list_item_single_choice,
            items
        ) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent) as CheckedTextView
            view.isChecked = position == selected
            view.setOnClickListener {
                selected = position
                notifyDataSetChanged()
            }
            return view
        }
    }

    class Builder {
        var selected: Int = 0
        var items: Collection<String> = listOf()

        var target: Fragment? = null
        var requestCode: Int = REQUEST_CODE
        var isCancelable: Boolean = true

        fun build() =
            RadioOptionsDialog().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_SELECTED, selected)
                    putStringArrayList(EXTRA_ITEMS, ArrayList(items))
                    putInt(EXTRA_REQUEST_CODE, this@Builder.requestCode)
                }
                if (target != null)
                    setTargetFragment(target, this@Builder.requestCode)
                this.isCancelable = this@Builder.isCancelable
            }
    }
}