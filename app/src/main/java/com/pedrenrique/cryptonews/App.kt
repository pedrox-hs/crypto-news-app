package com.pedrenrique.cryptonews

import android.app.Application
import com.pedrenrique.cryptonews.core.di.setUpDI

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setUp()
    }

    open fun setUp() {
        setUpDI()
    }
}