package com.core.sample.viewmodel

import androidx.lifecycle.ViewModel
import com.app.api.api.*
import com.core.sample.ShowCountriesDialog
import com.core.sample.ShowPickOfAppsDialog
import com.core.sample.ShowPickedApps
import com.core.sample.databinding.FrgHomeBinding
import com.core.sample.util.ColorRes
import com.core.sample.util.HomeTitle
import com.core.sample.util.StringRes
import com.core.sample.util.countriesdialog.CountriesHolder
import com.core.sample.util.countriesdialog.Country
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
    fun bindViewModelKey(wumfApi : WumfApi, countryHolder: CountriesHolder, headerInterceptor: HeaderInterceptor,
                         stringRes: StringRes, colorRes: ColorRes): ViewModel
            = HomeViewModel(wumfApi, countryHolder, headerInterceptor, stringRes, colorRes)
}

private const val IN_THE_WORLD = 0
const val IN_MY_COUNTRY = 1
const val IN_ANOTHER_COUNTRY = 2
private const val AMONG_FRIENDS = 3

class HomeViewModel @Inject constructor(private val wumfApi : WumfApi,
                                        private val countryHolder: CountriesHolder,
                                        private val headerInterceptor: HeaderInterceptor,
                                        stringRes: StringRes, colorRes: ColorRes): BaseViewModel<FrgHomeBinding>() {

    val span = HomeTitle(stringRes, colorRes)

    fun handleTestError() {
        handleException(Exception("Some error"))
    }

    fun showPickTypeOfAppsDialog() {
        postEvent(ShowPickOfAppsDialog(span.type))
    }

    fun pickedTypeOfApps(position: Int, country: Country?) {
        when (position) {
            IN_THE_WORLD -> {
                span.type = HomeTitle.Type.IN_THE_WORLD
                getAllWorldApps()
            }
            IN_MY_COUNTRY -> {
                val code = countryHolder.getDeviceCountryCode()
                span.countryName = getDefaultCountryName(code)
                getCountryApps(code)
                if (span.type == HomeTitle.Type.IN_COUNTRY) {
                    span.forciblyTextUpdate()
                } else {
                    span.type = HomeTitle.Type.IN_COUNTRY
                }
            }
            IN_ANOTHER_COUNTRY -> {
                if (country == null) {
                    postEvent(ShowCountriesDialog())
                    return
                }
                span.countryName = country.name
                getCountryApps(country.code)
                if (span.type == HomeTitle.Type.IN_COUNTRY) {
                    span.forciblyTextUpdate()
                } else {
                    span.type = HomeTitle.Type.IN_COUNTRY
                }
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

    fun getDefaultCountryName(): String {
        val code = countryHolder.getDeviceCountryCode()
        return getDefaultCountryName(code)
    }

    fun getDefaultCountry(): Country? {
        val code = countryHolder.getDeviceCountryCode()
        return getDefaultCountry(code)
    }

    private fun getDefaultCountryName(code: String): String {
        val country = getDefaultCountry(code)
        return country?.name ?: ""
    }

    private fun getDefaultCountry(code: String): Country? {
        val country = countryHolder.countries.find {
            it.code == code
        }
        return country
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

    fun getCountryApps(country: String) {
        startBgJob {
            callRetrofit(
                call = wumfApi.getNotMyApps(
                    GetNotMyAppsRequest(
                        inCountry = true,
                        country = country,
                        friends = emptyList()
                    )
                ),
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