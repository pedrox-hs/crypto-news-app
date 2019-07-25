package com.pedrenrique.cryptonews.core.ext

import android.content.Context
import com.pedrenrique.cryptonews.R
import java.util.*


fun Date.formatElapsedDays(context: Context, to: Calendar) =
    when (val elapsedDays = elapsedDays(to)) {
        0 -> context.getString(R.string.text_elapsed_today)
        1 -> context.getString(R.string.text_yesterday)
        in 2..7 -> context.getString(
            R.string.text_few_days,
            elapsedDays
        )
        else -> context.getString(
            R.string.text_many_days,
            asCalendar().getDisplayName(
                Calendar.MONTH,
                Calendar.SHORT,
                Locale.getDefault()
            ),
            asCalendar().get(Calendar.DAY_OF_MONTH).toString()
        )
    }

fun Date.elapsedDays(to: Calendar): Int {
    val date = asCalendar().resetTime()
    return (to.resetTime().timeInMillis - date.timeInMillis).toInt() / (24 * 60 * 60 * 1000)
}

fun Date.asCalendar() =
    Calendar.getInstance().apply {
        time = this@asCalendar
    }