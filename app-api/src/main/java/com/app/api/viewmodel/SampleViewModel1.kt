package com.app.api.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.app.api.api.*
import com.app.api.databinding.FrgSample1Binding
import com.library.core.BaseViewModel
import com.library.core.di.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Call
import java.lang.Exception
import javax.inject.Inject


@Module
class SampleModule1 {
    @Provides
    @IntoMap
    @ViewModelKey(SampleViewModel1::class)
    fun bindViewModelKey(wumfApi : WumfApi, headerInterceptor: HeaderInterceptor): ViewModel = SampleViewModel1(wumfApi, headerInterceptor)
}

class SampleViewModel1 @Inject constructor(private val wumfApi : WumfApi,
                                           private val headerInterceptor: HeaderInterceptor): BaseViewModel<FrgSample1Binding>() {

    val str = "fragment 1"

    val userId = ObservableField("")
    val password = ObservableField("")
    val appsPackages = ObservableField("")

    override fun handleException(e: Exception) { }

    fun checkRegistration() {
        startBgJob {
            callRetrofit(
                call = wumfApi.checkReg(CheckRegistrationRequest(userId.get()!!)),
                result = { response -> "hasInDb=${response?.hasInDb}" }
            )
        }
    }

    fun registration() {
        startBgJob {
            callRetrofit(
                call = wumfApi.registration(RegistrationRequest(userId.get()!!, "",
                    "${password.get().hashCode()}", "displayName", "ua"
                    )),
                result = { response ->
                    response?.token?.let {
                        headerInterceptor.updateToken(it)
                    }
                    return@callRetrofit "token=${response?.token}"
                }
            )
        }
    }

    fun login() {
        startBgJob {
            callRetrofit(
                call = wumfApi.login(LoginRequest(userId.get()!!, "", "${password.get().hashCode()}")),
                result = { response ->
                    response?.token?.let {
                        headerInterceptor.updateToken(it)
                    }
                    return@callRetrofit "token=${response?.token}"
                }
            )
        }
    }

    fun getApps() {
        startBgJob {
            callRetrofit(
                call = wumfApi.getApps(),
                result = { response -> "apps=${response?.apps?.getNullIfEmpty()?:"have no apps"}" }
            )
        }
    }

    fun addApp() {
        startBgJob {
            callRetrofit(
                call = wumfApi.addApp(AddAppRequest(appsPackages.get()!!)),
                result = { response -> "apps=${response?.apps?.getNullIfEmpty()?:"have no apps"}" }
            )
        }
    }

    fun removeApp() {
        startBgJob {
            callRetrofit(
                call = wumfApi.removeApp(RemoveAppRequest(appsPackages.get()!!)),
                result = { response -> "apps=${response?.apps?.getNullIfEmpty()?:"have no apps"}" }
            )
        }
    }

    fun getAllWorldApps() {
        startBgJob {
            callRetrofit(
                call = wumfApi.getNotMyApps(GetNotMyAppsRequest(allWorld = true, friends= emptyList())),
                result = { response -> "appsIsNotEmpty=${response?.apps?.isNotEmpty()}" }
            )
        }
    }

    fun getCountryApps() {
        return getCountryApps("ru")
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
                    //                fillApps(response)
                    "appsIsNotEmpty=${response?.apps?.isNotEmpty()}"
                }
            )
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