package com.core.sample.fragment

import com.core.sample.R
import com.core.sample.databinding.FrgHomeBinding
import com.core.sample.viewmodel.HomeViewModel
import com.library.core.BaseFragment

class HomeFragment : BaseFragment<FrgHomeBinding, HomeViewModel>() {

    override fun onInitVM() { }

    override fun getViewModelClass() = HomeViewModel::class

    override fun setViewModelInBinding(binding: FrgHomeBinding, viewModel: HomeViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes() = R.layout.frg_home

}