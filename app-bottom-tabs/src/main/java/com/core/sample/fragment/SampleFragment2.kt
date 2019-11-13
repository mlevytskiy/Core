package com.core.sample.fragment

import com.core.sample.R
import com.core.sample.databinding.FrgSample2Binding
import com.core.sample.viewmodel.SampleViewModel2
import com.library.core.BaseFragment
import kotlin.reflect.KClass

class SampleFragment2 : BaseFragment<FrgSample2Binding, SampleViewModel2>() {

    override fun getViewModelClass(): KClass<SampleViewModel2> {
        return SampleViewModel2::class
    }

    override fun setViewModelInBinding(binding: FrgSample2Binding, viewModel: SampleViewModel2) {
        binding.viewModel = viewModel
    }

    override fun onInitVM() { }

    override fun getLayoutRes(): Int {
        return R.layout.frg_sample_2
    }

}