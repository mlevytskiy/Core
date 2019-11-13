package com.core.sample.viewmodel

import androidx.lifecycle.ViewModel
import com.core.sample.fragment.SampleFragment1
import com.core.sample.fragment.SampleFragment1Directions
import com.library.core.BaseViewModel
import com.library.core.di.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import java.lang.Exception
import javax.inject.Inject


@Module
class SampleModule1 {
    @Provides
    @IntoMap
    @ViewModelKey(SampleViewModel1::class)
    fun bindViewModelKey(): ViewModel = SampleViewModel1()
}

class SampleViewModel1 @Inject constructor(): BaseViewModel() {

    val str = "fragment 1"

    fun handleTestError() {
        handleException(Exception("Some error"))
    }

    fun nextScreen() {
        navigate(SampleFragment1Directions.actionSample1ToSample2(), SampleViewModel1::class)
    }

    fun prevScreen() {
        //do nothing
    }

    override fun handleException(e: Exception) {

    }

}