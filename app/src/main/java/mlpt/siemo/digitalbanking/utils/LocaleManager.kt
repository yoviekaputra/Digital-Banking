package mlpt.siemo.digitalbanking.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import mlpt.siemo.digitalbanking.SPManager
import java.util.*


object LocaleManager {
    private const val SELECTED_LANGUAGE = "user_language"

    fun setLocale(context: Context): Context {
        return updateResources(context, getCurrentLanguage(context))
    }

    fun setNewLocale(context: Context, language: String) {
        persistLanguagePreference(context, language)
        updateResources(context, language)
    }

    private fun getCurrentLanguage(context: Context?): String =
        if (SPManager.getString(context, SELECTED_LANGUAGE).isEmpty()){
            "id"
        } else {
            SPManager.getString(context, SELECTED_LANGUAGE)
        }

    fun getLocale(context: Context?) : Locale = Locale(getCurrentLanguage(context))

    private fun persistLanguagePreference(context: Context, language: String) {
        SPManager.saveString(context, SELECTED_LANGUAGE, language)
    }

    private fun updateResources(context: Context, language: String): Context {
        var contextFun = context
        val locale = Locale(language)
        val resources = context.resources
        val configuration = Configuration(resources.configuration)

        Locale.setDefault(locale)
        configuration.setLocale(locale)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                configuration.setLocale(locale)
                val localeList = LocaleList(locale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
                contextFun = context.createConfigurationContext(configuration)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
                configuration.setLocale(locale)
                contextFun = context.createConfigurationContext(configuration)

            }
            else -> {
                configuration.locale = locale
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
        }
        return contextFun
    }
}