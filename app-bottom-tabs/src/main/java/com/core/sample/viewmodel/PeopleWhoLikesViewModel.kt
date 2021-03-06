package com.core.sample.viewmodel

import androidx.lifecycle.ViewModel
import com.core.sample.R
import com.core.sample.databinding.FrgPeopleWhoLikesBinding
import com.core.sample.databinding.FrgSample1Binding
import com.core.sample.databinding.FrgSample2Binding
import com.core.sample.memory.UserInfoRepository
import com.library.core.BaseViewModel
import com.library.core.di.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import java.lang.Exception
import javax.inject.Inject


@Module
class PeopleWhoLikesModule {
    @Provides
    @IntoMap
    @ViewModelKey(PeopleWhoLikesViewModel::class)
    fun bindViewModelKey(repository: UserInfoRepository): ViewModel = PeopleWhoLikesViewModel(repository)
}

class PeopleWhoLikesViewModel @Inject constructor(private var repository: UserInfoRepository): BaseViewModel<FrgPeopleWhoLikesBinding>() {

    fun handleTestError() {
        handleException(Exception("Some error"))
    }

    fun nextScreen() {
        //does nothing
    }

    fun prevScreen() {
        popTo(R.id.sample2, false)
    }

    override fun handleException(e: Exception) {

    }

    fun getStatusBarHeight() = repository.getStatusBarHeight()

}