package com.pedrenrique.cryptonews.core.platform

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.StringRes
import com.pedrenrique.cryptonews.R
import java.util.*

class LocaleManager(val context: Context) {
    companion object {
        private const val PREF_LANGUAGE = "pref_language"
    }

    fun restoreLanguage() =
        updateResources(getLanguage())

    fun setLanguage(language: Language): Context {
        persistLanguage(language)
        return updateResources(language)
    }

    fun getLanguage(): Language {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val savedLanguage = pref.getString(PREF_LANGUAGE, null)
        return Language.getByCodeOrDefault(savedLanguage)
    }

    private fun persistLanguage(language: Language) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        pref.edit().putString(PREF_LANGUAGE, language.code).apply()
    }

    private fun updateResources(language: Language): Context {
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesLocale(locale)
        } else updateResourcesLocaleLegacy(locale)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResourcesLocale(locale: Locale): Context {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLocaleLegacy(locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}

enum class Language(
    val code: String,
    @StringRes
    val displayText: Int
) {
    ENGLISH("en", R.string.language_en),
    PORTUGUESE("pt", R.string.language_pt);

    companion object {
        fun getByCodeOrDefault(code: String?, default: Language? = null): Language {
            for (value in values()) {
                if (value.code == code)
                    return value
            }
            return default ?: getByCodeOrDefault(Locale.getDefault().language, ENGLISH)
        }
    }
}