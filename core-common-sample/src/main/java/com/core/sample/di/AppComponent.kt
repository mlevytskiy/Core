package com.stuzo.dab.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.core.sample.App
import com.core.sample.MainActivity
import com.core.sample.fragment.SampleFragment1
import com.core.sample.fragment.SampleFragment2
import com.core.sample.viewmodel.SampleModule1
import com.core.sample.viewmodel.SampleModule2
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



@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            ActivityModule::class
            , FragmentsModule::class
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

    @ContributesAndroidInjector(modules = [SampleModule2::class])
    abstract fun bindSample2(): SampleFragment2



}
