package com.core.sample.fragment

import com.core.sample.R
import com.core.sample.databinding.FrgAppsBinding
import com.core.sample.databinding.FrgProfileBinding
import com.core.sample.viewmodel.AppsViewModel
import com.core.sample.viewmodel.ProfileViewModel
import com.library.core.BaseFragment

class ProfileFragment: BaseFragment<FrgProfileBinding, ProfileViewModel>() {

    override fun onInitVM() { }

    override fun getViewModelClass() = ProfileViewModel::class

    override fun setViewModelInBinding(binding: FrgProfileBinding, viewModel: ProfileViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes() = R.layout.frg_profile

}