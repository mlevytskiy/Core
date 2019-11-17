package com.library.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.EventBusException
import javax.inject.Inject
import kotlin.reflect.KClass



abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel>: DaggerFragment() {

    @JvmField @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    var viewModel: VM? = null

    var binding: B? = null

    private var navController: NavController? = null

    fun navigate(nav: NavDirections) {
//        log("nav to: $nav")
//        postEvent(FragmentNavigationDirection(nav, this::class))
    }

    fun popTo(id: Int, inclusive: Boolean = false) {
//        log("pop to: $id")
//        postEvent(PopBackTo(id, inclusive, this::class))
    }

    override fun onResume() {
        super.onResume()
        registerEventBus()
    }

    override fun onPause() {
        unregisterEventBus()
        super.onPause()
    }

    private fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            try {
                EventBus.getDefault().register(this)
            } catch (exception : EventBusException) {
                //ignore
            }
//            log("event bus registered for: " + this::class.simpleName)
        }
    }

    private fun unregisterEventBus() {
        EventBus.getDefault().unregister(this)
//        log("event bus unregistered for: " + this::class.simpleName)
    }

    abstract fun onInitVM()

    protected open fun getToolbar(): Toolbar? {
        return null
    }

    protected abstract fun getViewModelClass(): KClass<VM>

    protected abstract fun setViewModelInBinding(binding: B, viewModel: VM)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[getViewModelClass().java]
    }

    protected abstract fun getLayoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        setViewModelInBinding(binding!!, viewModel!!)
        getToolbar()?.let {
            (activity as AppCompatActivity).setSupportActionBar(it)
        }
        navController = findNavController()
        onInitVM()
        return binding?.root
    }

}