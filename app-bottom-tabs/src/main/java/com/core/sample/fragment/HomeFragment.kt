package com.core.sample.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.core.sample.R
import com.core.sample.ShowCountriesDialog
import com.core.sample.ShowPickOfAppsDialog
import com.core.sample.ShowPickedApps
import com.core.sample.databinding.FrgHomeBinding
import com.core.sample.util.countriesdialog.CountriesHolder
import com.core.sample.util.countriesdialog.Country
import com.core.sample.util.showInGooglePlay
import com.core.sample.viewmodel.HomeViewModel
import com.core.sample.viewmodel.IN_ANOTHER_COUNTRY
import com.core.sample.viewmodel.IN_MY_COUNTRY
import com.fortest.something.feature.onboarding.showAppDialog
import com.fortest.something.feature.onboarding.showCountriesDialog
import com.fortest.something.feature.onboarding.showSimpleDialog
import com.library.core.BaseFragment
import dagger.Provides
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class HomeFragment : BaseFragment<FrgHomeBinding, HomeViewModel>() {

    @JvmField @Inject
    var countriesHolder: CountriesHolder? = null

    override fun onInitVM() { }

    override fun getViewModelClass() = HomeViewModel::class

    override fun setViewModelInBinding(binding: FrgHomeBinding, viewModel: HomeViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes() = R.layout.frg_home

    @Subscribe
    fun onEvent(event: ShowPickOfAppsDialog) {
        viewModel?.let {viewModel ->
            showSimpleDialog(requireContext(), R.array.type_of_apps, -1,
                { array, pos ->
                    val country: Country? = if (pos == IN_MY_COUNTRY) viewModel.getDefaultCountry()
                                            else null
                    viewModel.pickedTypeOfApps(pos, country)
                }, {
                    it.dismiss()
                }, viewModel.getDefaultCountryName())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ShowPickedApps) {
        binding?.appsRecycleView?.setPackages(event.appPackages, event.likes)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        var dialog : DialogInterface? = null
        binding?.appsRecycleView?.setItemClick { app, likes->
            dialog = showAppDialog(app, container!!.context,
            {
                dialog?.dismiss()
                showInGooglePlay(app.packageName, container.context)
            }, {
                    dialog?.dismiss()
                    viewModel?.navigateToPeopleWhoLikes()
                }, likes) }
        return view
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ShowCountriesDialog) {
        var dialog: DialogInterface? = null
        countriesHolder?.let {
            if (it.countries.isEmpty()) {
                it.syncLoad()
            }
            dialog = showCountriesDialog(requireContext(), it.countries, -1, {country ->
                dialog?.dismiss()
                viewModel?.pickedTypeOfApps(IN_ANOTHER_COUNTRY, country)
            }, {})
        }
    }

}