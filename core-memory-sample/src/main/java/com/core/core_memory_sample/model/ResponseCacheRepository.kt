package com.core.core_memory_sample.model

import java.util.*

interface ResponseCacheRepository {

    fun set(date: Date?)
    fun get(): Date?
    fun isEmpty(): Boolean

    fun getObj(): SomeObj?
    fun setObj(someObj: SomeObj?)
}