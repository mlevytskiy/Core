package com.core.sample.fragment

import com.core.sample.R
import com.core.sample.ShowPickOfAppsDialog
import com.core.sample.ShowPickedApps
import com.core.sample.databinding.FrgHomeBinding
import com.core.sample.viewmodel.HomeViewModel
import com.fortest.something.feature.onboarding.showSimpleDialog
import com.library.core.BaseFragment
import org.greenrobot.eventbus.Subscribe

class HomeFragment : BaseFragment<FrgHomeBinding, HomeViewModel>() {

    override fun onInitVM() { }

    override fun getViewModelClass() = HomeViewModel::class

    override fun setViewModelInBinding(binding: FrgHomeBinding, viewModel: HomeViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes() = R.layout.frg_home

    @Subscribe
    fun onEvent(event: ShowPickOfAppsDialog) {
        showSimpleDialog(requireContext(), R.array.type_of_apps, event.type.ordinal,
            { array, pos ->
                viewModel?.pickedTypeOfApps(pos)
            }, {
                it.dismiss()
            })
    }

    @Subscribe
    fun onEvent(event: ShowPickedApps) {
        binding?.appsRecycleView?.setPackages(event.appPackages)
    }

}