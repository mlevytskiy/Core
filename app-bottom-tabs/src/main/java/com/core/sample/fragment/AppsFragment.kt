package com.core.sample.fragment

import com.core.sample.R
import com.core.sample.databinding.FrgAppsBinding
import com.core.sample.viewmodel.AppsViewModel
import com.library.core.BaseFragment

class AppsFragment: BaseFragment<FrgAppsBinding, AppsViewModel>() {

    override fun onInitVM() { }

    override fun getViewModelClass() = AppsViewModel::class

    override fun setViewModelInBinding(binding: FrgAppsBinding, viewModel: AppsViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes() = R.layout.frg_apps

}