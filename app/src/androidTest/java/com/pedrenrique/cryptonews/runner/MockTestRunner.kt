package com.pedrenrique.cryptonews.runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.pedrenrique.cryptonews.TestApp

class MockTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application = super.newApplication(cl, TestApp::class.java.name, context)
}