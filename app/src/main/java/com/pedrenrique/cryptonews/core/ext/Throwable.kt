package com.pedrenrique.cryptonews.core.ext

import com.pedrenrique.cryptonews.R
import java.io.IOException

val Throwable?.isNetworkProblem: Boolean
    get() = this is IOException

val Throwable?.defaultFriendlyTitle: Int
    get() = if (isNetworkProblem) {
        R.string.title_error_connection
    } else {
        R.string.title_error_unexpected
    }

val Throwable?.defaultFriendlyMsg: Int
    get() = if (isNetworkProblem) {
        R.string.msg_error_connection
    } else {
        R.string.msg_error_unexpected
    }

val Throwable?.defaultErrorDrawable: Int
    get() = if (isNetworkProblem) {
        R.drawable.ic_cloud_off
    } else {
        R.drawable.ic_robot_error
    }