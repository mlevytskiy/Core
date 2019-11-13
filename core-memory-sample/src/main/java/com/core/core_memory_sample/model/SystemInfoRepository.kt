package com.core.core_memory_sample.model

interface SystemInfoRepository {

    fun set(height: Int)
    fun get(): Int
    fun isEmpty(): Boolean

}