package com.core.sample.fragment

import com.core.sample.R
import com.core.sample.databinding.FrgAppsBinding
import com.core.sample.databinding.FrgFriendsBinding
import com.core.sample.viewmodel.AppsViewModel
import com.core.sample.viewmodel.FriendsViewModel
import com.library.core.BaseFragment

class FriendsFragment: BaseFragment<FrgFriendsBinding, FriendsViewModel>() {

    override fun onInitVM() { }

    override fun getViewModelClass() = FriendsViewModel::class

    override fun setViewModelInBinding(binding: FrgFriendsBinding, viewModel: FriendsViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes() = R.layout.frg_friends

}