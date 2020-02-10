package com.core.sample.fragment

import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import com.core.sample.*
import com.core.sample.databinding.FrgEnterPhoneNumberBinding
import com.core.sample.viewmodel.EnterPhoneNumberViewModel
import com.library.core.BaseFragment
import com.onboarding.enterphonenumberui.CodeEditText
import com.onboarding.enterphonenumberui.LabeledEditText
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.reflect.KClass

class EnterPhoneNumberFragment: BaseFragment<FrgEnterPhoneNumberBinding, EnterPhoneNumberViewModel>() {

    override fun onInitVM() {
        binding?.phoneNumberFlipWrapper?.phoneNumberChangeListener = object : LabeledEditText.PhoneNumberChangeListener {
            override fun onChange(str: String) {
                viewModel?.phoneNumber = str
            }
        }
        binding?.phoneNumberFlipWrapper?.codeChangeListener = object : CodeEditText.CodeChangeListener {
            override fun onChange(str: String) {
                viewModel?.code = str
            }
        }
        val args: EnterPhoneNumberFragmentArgs by navArgs()
        if (args.phoneNumber.isNullOrEmpty()) {
            viewModel?.getSystemPhoneNumber()?.let {
                binding?.phoneNumberFlipWrapper?.enterPhoneByUser(it)
            }
        } else {
            args.phoneNumber?.let {
                binding?.phoneNumberFlipWrapper?.enterPhoneByUser(it)
            }
        }
    }

    override fun getViewModelClass(): KClass<EnterPhoneNumberViewModel> = EnterPhoneNumberViewModel::class

    override fun setViewModelInBinding(
        binding: FrgEnterPhoneNumberBinding,
        viewModel: EnterPhoneNumberViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.frg_enter_phone_number

    override fun getToolbar(): Toolbar? {
        val toolbar = binding?.toolbar
        toolbar?.title = ""
        return toolbar
    }

    @Subscribe
    fun onEvent(progressState: ShowNextButtonInProgressState) {
        binding?.nextButton?.setIndeterminateProgressMode(true)
        binding?.nextButton?.progress = 50
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(showEnterCodeState: ShowEnterCodeState) {
        binding?.phoneNumberFlipWrapper?.showBackCard()
        binding?.nextButton?.progress = 0
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(showSuccess: ShowSuccess) {
        binding?.nextButton?.progress = 100
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(moveToHomeScreen: MoveToHomeScreen) {
        Toast.makeText(requireContext(), "move to next screen", Toast.LENGTH_LONG).show()
    }

}