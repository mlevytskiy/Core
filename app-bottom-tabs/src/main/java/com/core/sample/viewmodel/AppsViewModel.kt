package com.core.sample.viewmodel

import androidx.lifecycle.ViewModel
import com.core.sample.databinding.FrgAppsBinding
import com.core.sample.fragment.SampleFragment1Directions
import com.library.core.BaseViewModel
import com.library.core.di.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import java.lang.Exception
import javax.inject.Inject

@Module
class AppsModule {
    @Provides
    @IntoMap
    @ViewModelKey(AppsViewModel::class)
    fun bindViewModelKey(): ViewModel = AppsViewModel()
}

class AppsViewModel @Inject constructor(): BaseViewModel<FrgAppsBinding>() {

    fun handleTestError() {
        handleException(Exception("Some error"))
    }

    override fun handleException(e: Exception) {

    }

}