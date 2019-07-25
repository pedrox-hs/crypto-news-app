package com.pedrenrique.cryptonews.core.di

import android.app.Application
import com.pedrenrique.cryptonews.BuildConfig
import com.pedrenrique.cryptonews.core.di.modules.appModule
import com.pedrenrique.cryptonews.core.di.modules.netModule
import com.pedrenrique.cryptonews.core.di.modules.newsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

const val API_BASE_URL = "API_BASE_URL"
const val ENABLE_HTTP_LOG = "ENABLE_HTTP_LOG"

fun Application.setUpDI() {
    startKoin {
        androidLogger()

        androidContext(this@setUpDI)

        properties(
            mapOf(
                API_BASE_URL to BuildConfig.API_BASE_URL,
                ENABLE_HTTP_LOG to BuildConfig.DEBUG
            )
        )

        modules(
            listOf(
                appModule,
                netModule,
                newsModule
            )
        )
    }
}