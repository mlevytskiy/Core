package com.core.sample.fragment

import android.app.Activity
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import com.core.sample.R
import com.core.sample.ShowDetectedPhoneNumberEvent
import com.core.sample.databinding.FrgWaitYourPhoneNumberDetectingBinding
import com.core.sample.viewmodel.WaitYourPhoneNumberDetectingViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.library.core.BaseFragment
import com.library.core.event.HandleOnActivityResult
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.reflect.KClass


class WaitYourPhoneNumberDetectingFragment: BaseFragment<FrgWaitYourPhoneNumberDetectingBinding, WaitYourPhoneNumberDetectingViewModel>() {

    private var googleApiClient: GoogleApiClient? = null
    private val REQUEST_PHONE_NUMBER_CODE = 565

    override fun onInitVM() {
        if (viewModel?.isNeedDetectPhoneNumber() == true) {
            googleApiClient = GoogleApiClient
                .Builder(requireContext())
                .enableAutoManage(
                    requireActivity(),
                    object : GoogleApiClient.OnConnectionFailedListener {
                        override fun onConnectionFailed(connectionResult: ConnectionResult) {
                            viewModel?.cantDetectPhoneNumber()
                        }
                    })
                .addApi(Auth.CREDENTIALS_API)
                .build()
        } else {
            viewModel?.nextScreen(navController = navController)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PHONE_NUMBER_CODE && resultCode == Activity.RESULT_OK) {
            val credentials = data?.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
            credentials?.id?.let {
                viewModel?.detectPhoneNumber(it)
                return
            }
        }
        viewModel?.cantDetectPhoneNumber()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showDetectPhoneNumberHint(event: ShowDetectedPhoneNumberEvent) {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()
        val intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest)
        EventBus.getDefault().post(HandleOnActivityResult(this))
        activity?.startIntentSenderForResult(intent.intentSender,
            REQUEST_PHONE_NUMBER_CODE, null, 0, 0, 0,  null)
    }

    override fun getViewModelClass(): KClass<WaitYourPhoneNumberDetectingViewModel> = WaitYourPhoneNumberDetectingViewModel::class

    override fun setViewModelInBinding(
        binding: FrgWaitYourPhoneNumberDetectingBinding,
        viewModel: WaitYourPhoneNumberDetectingViewModel) {
        binding.viewModel = viewModel
    }

    override fun getLayoutRes(): Int = R.layout.frg_wait_your_phone_number_detecting

    override fun getToolbar(): Toolbar? {
        val toolbar = binding?.toolbar
        toolbar?.title = ""
        return toolbar
    }

    override fun onResume() {
        super.onResume()
        googleApiClient?.connect()
        if (viewModel?.isPhoneNumberDetectingStarted() != true) {
            viewModel?.startPhoneNumberDetecting()
        } else {
            viewModel?.resumePhoneNumberDetecting()
        }
    }

    override fun onPause() {
        super.onPause()
        googleApiClient?.stopAutoManage(requireActivity())
        googleApiClient?.disconnect()
        viewModel?.interruptPhoneNumberDetecting()
    }

}