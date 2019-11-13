package com.library.core

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.library.core.event.FragmentNavigationDirection
import com.library.core.event.ShowToastEvent
import dagger.android.support.DaggerAppCompatActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity(val layoutRes: Int) : DaggerAppCompatActivity() {

    protected lateinit var navController: NavController

    protected abstract fun getNavRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        init()
    }

    fun init() {
        if (getNavRes() != 0) {
            navController = Navigation.findNavController(this, getNavRes())
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun navigate(event: FragmentNavigationDirection) {
        navController.navigate(event.nav)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showToast(event: ShowToastEvent) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
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
//        log("registerEventBus call for: " + this::class.simpleName)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
//            log("event bus registered for: " + this::class.simpleName)
        }
    }

    private fun unregisterEventBus() {
        EventBus.getDefault().unregister(this)
//        log("event bus UNregistered for: " + this::class.simpleName)
    }

}