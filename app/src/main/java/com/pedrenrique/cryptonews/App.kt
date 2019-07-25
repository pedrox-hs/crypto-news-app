package com.pedrenrique.cryptonews

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.pedrenrique.cryptonews.core.di.setUpDI
import com.pedrenrique.cryptonews.core.platform.LocaleManager


open class App : Application() {
    lateinit var localeManager: LocaleManager

    override fun onCreate() {
        super.onCreate()
        setUp()
    }

    override fun attachBaseContext(base: Context) {
        localeManager = LocaleManager(base)
        super.attachBaseContext(localeManager.restoreLanguage())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager.restoreLanguage()
    }

    open fun setUp() {
        setUpDI()
    }
}