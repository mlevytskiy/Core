package com.core.sample


import com.core.sample.memory.UserInfoRepository
import com.stuzo.dab.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class App: DaggerApplication() {

    @JvmField @Inject
    var repository: UserInfoRepository? = null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        val appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        appComponent.inject(this)

        return appComponent
    }
}