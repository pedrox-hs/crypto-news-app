package com.pedrenrique.cryptonews

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.pedrenrique.cryptonews.core.di.setUpDI
import com.pedrenrique.cryptonews.core.platform.LocaleManager


open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setUp()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.restoreLanguage(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManager.restoreLanguage(this)
    }

    open fun setUp() {
        setUpDI()
    }
}