package com.core.sample.fragment

import com.core.sample.R
import com.core.sample.databinding.FrgAppsBinding
import com.core.sample.databinding.FrgFriendsBinding
import com.core.sample.databinding.FrgMoreBinding
import com.core.sample.viewmodel.AppsViewModel
import com.core.sample.viewmodel.FriendsViewModel
import com.core.sample.viewmodel.MoreViewModel
import com.library.core.BaseFragment

class MoreFragment: BaseFragment<FrgMoreBinding, MoreViewModel>() {

    override fun onInitVM() { }

    override fun getViewModelClass() = MoreViewModel::class

    override fun setViewModelInBinding(binding: FrgMoreBinding, viewModel: MoreViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes() = R.layout.frg_more

}