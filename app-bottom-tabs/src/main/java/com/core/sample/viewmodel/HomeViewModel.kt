package com.core.sample.viewmodel

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.app.api.api.*
import com.core.sample.ShowPickOfAppsDialog
import com.core.sample.ShowPickedApps
import com.core.sample.fragment.SampleFragment1Directions
import com.library.core.BaseViewModel
import com.library.core.di.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Call
import java.lang.Exception
import javax.inject.Inject


@Module
class HomeModule {
    @Provides
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindViewModelKey(wumfApi : WumfApi, headerInterceptor: HeaderInterceptor): ViewModel = HomeViewModel(wumfApi, headerInterceptor)
}

private const val IN_THE_WORLD = 0
private const val IN_COUNTRY = 1
private const val AMONG_FRIENDS = 2

class HomeViewModel @Inject constructor(private val wumfApi : WumfApi,
                                        private val headerInterceptor: HeaderInterceptor
): BaseViewModel() {

    val span = ObservableField(SpannableString("Find the best apps in the world"))//AndroidTagBadgeBuilder(SpannableStringBuilder("Find the best apps in the world"), 40, "in the world").tags
    private var textSuffix = "Find the best apps\n"
    private var highlightText = "in the world"
    private var pickedTypeOfApps = IN_THE_WORLD

    init {
        updateText()
    }

    fun handleTestError() {
        handleException(Exception("Some error"))
    }

    private fun updateText() {
        val spannable = SpannableString(textSuffix + highlightText)
        spannable.setSpan(BackgroundColorSpan(Color.GREEN), textSuffix.length, textSuffix.length + highlightText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.set(spannable)
    }

    fun showPickTypeOfAppsDialog() {
        postEvent(ShowPickOfAppsDialog(pickedTypeOfApps))
    }

    fun pickedTypeOfApps(str: String, position: Int) {
        when (position) {
            IN_THE_WORLD -> {
                getAllWorldApps()
            }
            IN_COUNTRY -> {

            }
            AMONG_FRIENDS -> {

            }
        }
        pickedTypeOfApps = position
        highlightText = str
        updateText()

    }

    override fun handleException(e: Exception) {

    }

    fun getAllWorldApps() {
        startBgJob {
            callRetrofit(
                call = wumfApi.getNotMyApps(GetNotMyAppsRequest(allWorld = true, friends= emptyList())),
                result = { response ->
                    fillApps(response)
                    "appsIsNotEmpty=${response?.apps?.isNotEmpty()}"
                }
            )
        }
    }

    private fun fillApps(response: GetNotMyAppsResponse?) {
        response?.let {
            val appsStr = prepareAppsForAdapter(it.apps)
            postEvent(ShowPickedApps(appsStr))
        } ?: kotlin.run {
            showToast("response is null", HomeViewModel::class)
        }
    }

    private fun prepareAppsForAdapter(apps: List<App>): String {
        if (apps.isEmpty()) {
            return ""
        } else {
            return apps.map { it.packageName }.joinToString(",")
        }
    }

    private fun <T> callRetrofit(call: Call<T>, result: (T?)->(String)) {
        val response = executeRetrofit(call=call,
            generalError = { e -> showToast("error=" + e.message, HomeViewModel::class) })
        response?.let{
            showToast(result.invoke(it), HomeViewModel::class)
        }
    }

}