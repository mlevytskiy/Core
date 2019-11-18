package com.core.core_memory_sample.model

import java.util.*

interface ResponseCacheRepository {

    fun set(id:Long, date: Date?)
    fun get(id: Long): Date?
    fun isEmpty(): Boolean

}