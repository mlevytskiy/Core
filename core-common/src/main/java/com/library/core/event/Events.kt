package com.library.core.event

import androidx.navigation.NavDirections
import com.library.core.BaseViewModel
import kotlin.reflect.KClass

class FragmentNavigationDirection(val nav: NavDirections) : BaseEvent()

class PopBackTo(val id: Int, val inclusive: Boolean) : BaseEvent()

class ShowToastEvent(val message: String) : BaseEvent()