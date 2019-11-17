package com.core.sample.fragment

import com.core.sample.R
import com.core.sample.TagSpannableBuilder
import com.core.sample.databinding.FrgSample1Binding
import com.core.sample.viewmodel.SampleViewModel1
import com.library.core.BaseFragment
import kotlin.reflect.KClass

class SampleFragment1 : BaseFragment<FrgSample1Binding, SampleViewModel1>() {

    override fun getViewModelClass(): KClass<SampleViewModel1> {
        return SampleViewModel1::class
    }

    override fun setViewModelInBinding(binding: FrgSample1Binding, viewModel: SampleViewModel1) {
        binding.viewModel = viewModel
    }

    override fun onInitVM() {
        val color1 = resources.getColor(R.color.gray2)
        val color2 = resources.getColor(R.color.gray1)
        val builder = TagSpannableBuilder("tratatat ta", color1, color2)
        builder.appendTag("trrrs")
        binding?.textView?.text = builder.build()
    }

    override fun getLayoutRes(): Int {
        return R.layout.frg_sample_1
    }

}