package com.core.sample.util

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import java.util.*

object LocaleHelper {

    fun setLocale(
        context: Context,
        lang: String
    ): Context = updateLanguage(context, lang)
//    {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            updateResources(context, lang)
//        } else updateResourcesLegacy(context, lang)
//    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(
        context: Context,
        language: String
    ): Context {
        val locale = Locale(language, language)
        Locale.setDefault(locale)
        val configuration =
            context.resources.configuration
        configuration.setLocale(locale)
        Resources.getSystem()
            .updateConfiguration(configuration, context.resources.displayMetrics)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(
        context: Context,
        language: String
    ): Context {
        val locale = Locale(language, language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        Resources.getSystem()
            .updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    private fun updateLanguage(context: Context, language: String): Context {
        val res = context.resources
        val dm = res.getDisplayMetrics()
        val conf = res.getConfiguration()
        val locale = Locale(language, language)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(locale)
        } else {
            conf.locale = locale
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(conf)
        } else {
            context.resources.updateConfiguration(conf, dm)
        }
        return context
    }

}