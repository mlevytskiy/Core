package com.app.api.fragment

import com.app.api.R
import com.app.api.databinding.FrgSample1Binding
import com.app.api.viewmodel.SampleViewModel1
import com.library.core.BaseFragment
import kotlin.reflect.KClass

class SampleFragment1 : BaseFragment<FrgSample1Binding, SampleViewModel1>() {

    override fun getViewModelClass(): KClass<SampleViewModel1> {
        return SampleViewModel1::class
    }

    override fun setViewModelInBinding(binding: FrgSample1Binding, viewModel: SampleViewModel1) {
        binding.viewModel = viewModel
    }

    override fun onInitVM() { }

    override fun getLayoutRes(): Int {
        return R.layout.frg_sample_1
    }

}