package com.core.sample.viewmodel

import androidx.lifecycle.ViewModel
import com.core.sample.R
import com.core.sample.databinding.FrgSample1Binding
import com.core.sample.databinding.FrgSample2Binding
import com.library.core.BaseViewModel
import com.library.core.di.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import java.lang.Exception
import javax.inject.Inject


@Module
class SampleModule2 {
    @Provides
    @IntoMap
    @ViewModelKey(SampleViewModel2::class)
    fun bindViewModelKey(): ViewModel = SampleViewModel2()
}

class SampleViewModel2 @Inject constructor(): BaseViewModel<FrgSample2Binding>() {

    val str = "fragment 2"

    fun handleTestError() {
        handleException(Exception("Some error"))
    }

    fun nextScreen() {
        //do nothing
    }

    fun prevScreen() {
        popTo(R.id.sample2, false)
    }

    override fun handleException(e: Exception) {

    }

}