package com.library.core.event

import java.io.Serializable
import kotlin.reflect.KClass

open class BaseEvent(var senderClass: KClass<out Any>? = null) : Serializable