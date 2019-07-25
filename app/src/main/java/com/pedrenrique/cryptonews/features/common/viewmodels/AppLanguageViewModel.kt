package com.pedrenrique.cryptonews.features.common.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pedrenrique.cryptonews.core.platform.Language
import com.pedrenrique.cryptonews.core.platform.LocaleManager

class AppLanguageViewModel(private val localeManager: LocaleManager) : ViewModel() {
    val state = MutableLiveData<State>(
        State.Done
    )

    var actualLanguage = localeManager.getLanguage().ordinal
        set(value) {
            if (value != field && value >= 0 && value < availableLanguageOptions.size) {
                field = value
                localeManager.setLanguage(language)
                state.value =
                    State.RefreshNeeded
            }
        }

    private var language: Language
        get() = availableLanguageOptions[actualLanguage]
        set(value) {
            actualLanguage = value.ordinal
        }

    val availableLanguageOptions = Language.values().asList()

    fun setNewLanguage(language: Int) {
        actualLanguage = language
    }

    fun setRefreshDone() {
        state.value =
            State.Done
    }

    sealed class State {
        object Done : State()
        object RefreshNeeded : State()
    }
}