package com.core.sample.fragment

import androidx.appcompat.widget.Toolbar
import com.core.sample.R
import com.core.sample.databinding.FrgPeopleWhoLikesBinding
import com.core.sample.databinding.FrgSample2Binding
import com.core.sample.viewmodel.PeopleWhoLikesViewModel
import com.core.sample.viewmodel.SampleViewModel2
import com.library.core.BaseFragment
import kotlin.reflect.KClass

class PeopleWhoLikesFragment : BaseFragment<FrgPeopleWhoLikesBinding, PeopleWhoLikesViewModel>() {

    override fun getViewModelClass(): KClass<PeopleWhoLikesViewModel> {
        return PeopleWhoLikesViewModel::class
    }

    override fun setViewModelInBinding(binding: FrgPeopleWhoLikesBinding, viewModel: PeopleWhoLikesViewModel) {
        binding.viewModel = viewModel
    }

    override fun onInitVM() { }

    override fun getLayoutRes(): Int {
        return R.layout.frg_people_who_likes
    }

    override fun getToolbar(): Toolbar? {
        val toolbar = binding?.dbToolbar
        toolbar?.setTitle("")
        return toolbar
    }

}