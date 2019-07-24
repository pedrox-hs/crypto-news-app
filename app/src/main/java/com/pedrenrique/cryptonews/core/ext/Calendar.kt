package com.pedrenrique.cryptonews.core.ext

import java.util.*

fun Calendar.resetTime(): Calendar {
    val c = clone() as Calendar
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
    return c
}