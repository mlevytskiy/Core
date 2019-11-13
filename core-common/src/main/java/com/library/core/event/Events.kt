package com.library.core.event

import androidx.navigation.NavDirections
import com.library.core.BaseViewModel
import kotlin.reflect.KClass

class FragmentNavigationDirection(val nav: NavDirections, senderClass: KClass<out BaseViewModel>) : BaseEvent(senderClass)

class PopBackTo(val id: Int, val inclusive: Boolean, senderClass: KClass<out BaseViewModel>) : BaseEvent(senderClass)

class ShowToastEvent(val message: String, senderClass: KClass<out BaseViewModel>) : BaseEvent(senderClass)