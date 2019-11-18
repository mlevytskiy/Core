package com.library.core

import androidx.lifecycle.ViewModel
import androidx.databinding.*
import androidx.navigation.NavDirections
import com.library.core.event.BaseEvent
import com.library.core.event.FragmentNavigationDirection
import com.library.core.event.PopBackTo
import com.library.core.event.ShowToastEvent
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

abstract class BaseViewModel<B : ViewDataBinding>: ViewModel(), Observable, HoldUI<B> {

    @Transient
    private var callbacks: PropertyChangeRegistry? = null
    @Transient
    private val supervisor = SupervisorJob()
    @Transient
    private var scope = CoroutineScope(Dispatchers.IO + supervisor)

    private var weakReferenceUI: WeakReference<B>? = null

    override fun setUI(binding: B?) {
        binding?.let {
            weakReferenceUI = WeakReference(it)
        }
    }

    override fun getUI(): WeakReference<B>? {
        return weakReferenceUI
    }

    override fun needHoldUI(): Boolean {
        return false
    }

    override fun onCleared() {
        super.onCleared()
        cancelBgJobs()
    }

    open fun shouldHoldUI(): Boolean {
        return false
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if (callbacks == null) {
                return
            }
        }
        callbacks?.remove(callback)
    }

    fun showToast(message: String) {
        postEvent(ShowToastEvent(message))
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        synchronized(this) {
            if (callbacks == null) {
                callbacks = PropertyChangeRegistry()
            }
        }
        callbacks?.add(callback)
    }

    abstract fun handleException(e: Exception)

    fun startBgJob(handleError: ErrorHandlerMode = ErrorHandlerMode.HANDLE, block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(block = {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                when (handleError) {
                    ErrorHandlerMode.HANDLE -> {
                        handleException(e)
                    }
                    ErrorHandlerMode.THROW -> throw e
                    ErrorHandlerMode.IGNORE -> {}
                }
            }
        })
    }

    suspend fun <T> runMain(block: suspend CoroutineScope.() -> T): T =
        withContext(Dispatchers.Main, block)

    private fun cancelBgJobs() {
        supervisor.cancelChildren()
    }

    fun navigate(nav: NavDirections) {
        postEvent(FragmentNavigationDirection(nav))
    }

    fun popTo(id: Int, inclusive: Boolean = false) {
        postEvent(PopBackTo(id, inclusive))
    }

    fun postEvent(event: BaseEvent) {
        EventBus.getDefault().post(event)
    }

    enum class ErrorHandlerMode {
        HANDLE, IGNORE, THROW
    }
}