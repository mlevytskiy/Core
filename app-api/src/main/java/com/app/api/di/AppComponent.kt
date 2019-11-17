package com.app.api.di

import android.app.Application
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.api.App
import com.app.api.BuildConfig
import com.app.api.MainActivity
import com.app.api.api.HeaderInterceptor
import com.app.api.api.WumfApi
import com.app.api.fragment.SampleFragment1
import com.app.api.viewmodel.SampleModule1
import com.library.core.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Provider
import javax.inject.Singleton
import dagger.android.ContributesAndroidInjector
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            ActivityModule::class,
            FragmentsModule::class,
            RetrofitModule::class
        ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideViewModelFactory(
                providers: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory = ViewModelFactory(providers)
    }
}

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeProductListActivity(): MainActivity
}

@Module
abstract class FragmentsModule {

    @ContributesAndroidInjector(modules = [SampleModule1::class])
    abstract fun bindSample1(): SampleFragment1

}

private const val API_KEY_HEADER_KEY = "x-api-key"
private const val DEVICE_ID_HEADER_KEY = "device-id"
private const val APP_VERSION_HEADER_KEY = "AppVersion"
private const val TRACE_ID_HEADER_KEY = "trace-id"

@Module
class RetrofitModule {

    private val BASE_URL = "https://radiant-plains-90522.herokuapp.com/"
//    private val BASE_URL = "http://192.168.0.105:8080/"

    @Singleton
    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideOkHttpClient(headers: HeaderInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(headers)
        builder.addInterceptor (
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                Log.i("mynetwork", message)
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideHeaderInterceptor(context: Context): HeaderInterceptor {
        return HeaderInterceptor(HashMap(mapOf(
            DEVICE_ID_HEADER_KEY to Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            ),
            APP_VERSION_HEADER_KEY to "Android/${BuildConfig.VERSION_CODE}/${BuildConfig.VERSION_NAME}"
        )))
    }

    @Provides
    fun getWumfApi(retrofit: Retrofit): WumfApi {
        return retrofit.create(WumfApi::class.java)
    }

}
