package com.core.sample.viewmodel

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.app.api.api.*
import com.core.sample.ShowPickOfAppsDialog
import com.core.sample.ShowPickedApps
import com.core.sample.databinding.FrgHomeBinding
import com.core.sample.util.ColorRes
import com.core.sample.util.HomeTitle
import com.core.sample.util.StringRes
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
    fun bindViewModelKey(wumfApi : WumfApi, headerInterceptor: HeaderInterceptor,
                         stringRes: StringRes, colorRes: ColorRes): ViewModel
            = HomeViewModel(wumfApi, headerInterceptor, stringRes, colorRes)
}

private const val IN_THE_WORLD = 0
private const val IN_COUNTRY = 1
private const val AMONG_FRIENDS = 2

class HomeViewModel @Inject constructor(private val wumfApi : WumfApi,
                                        private val headerInterceptor: HeaderInterceptor,
                                        stringRes: StringRes, colorRes: ColorRes): BaseViewModel<FrgHomeBinding>() {

    val span = HomeTitle(stringRes, colorRes)

    fun handleTestError() {
        handleException(Exception("Some error"))
    }

    fun showPickTypeOfAppsDialog() {
        postEvent(ShowPickOfAppsDialog(span.type))
    }

    fun pickedTypeOfApps(position: Int) {
        when (position) {
            IN_THE_WORLD -> {
                span.type = HomeTitle.Type.IN_THE_WORLD
                getAllWorldApps()
            }
            IN_COUNTRY -> {
                span.type = HomeTitle.Type.IN_COUNTRY

            }
            AMONG_FRIENDS -> {
                span.type = HomeTitle.Type.AMONG_FRIENDS
            }
        }

    }

    override fun needHoldUI(): Boolean {
        return true
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
            showToast("response is null")
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
            generalError = { e -> showToast("error=" + e.message) })
        response?.let{
            showToast(result.invoke(it))
        }
    }

}