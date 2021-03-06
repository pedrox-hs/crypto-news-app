package com.pedrenrique.cryptonews.core.di.modules

import android.content.Context
import com.pedrenrique.cryptonews.App
import com.pedrenrique.cryptonews.features.common.viewmodels.AppLanguageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        AppLanguageViewModel(get())
    }

    single {
        (get<Context>().applicationContext as App).localeManager
    }
}