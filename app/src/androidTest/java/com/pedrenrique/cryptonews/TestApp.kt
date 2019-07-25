package com.pedrenrique.cryptonews

import com.pedrenrique.cryptonews.core.di.API_BASE_URL
import com.pedrenrique.cryptonews.core.di.ENABLE_HTTP_LOG
import com.pedrenrique.cryptonews.core.di.modules.appModule
import com.pedrenrique.cryptonews.core.di.modules.netModule
import com.pedrenrique.cryptonews.core.di.modules.newsModule
import com.pedrenrique.cryptonews.config.MOCK_SERVER_URL
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TestApp : App() {
    override fun setUp() {
        startKoin {
            androidLogger()

            androidContext(this@TestApp)

            properties(
                mapOf(
                    API_BASE_URL to MOCK_SERVER_URL,
                    ENABLE_HTTP_LOG to true
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
}