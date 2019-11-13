package com.core.core_memory_sample.model

import android.content.Context
import io.objectbox.Box
import io.objectbox.kotlin.query

class SystemInfoRepositoryImpl(context: Context):
    SystemInfoRepository {

    private val systemInfoBox: Box<SystemInfo>

    init {
        val boxStore = MyObjectBox.builder().androidContext(context).buildDefault()
        systemInfoBox = boxStore.boxFor(SystemInfo::class.java)
    }

    override fun isEmpty() = systemInfoBox.isEmpty

    override fun get() = systemInfoBox.query {
        equal(SystemInfo_.id, SystemInfo.ID)
    }.findFirst()?.statusBarHeight ?: 0

    override fun set(height: Int) {
        systemInfoBox.put(SystemInfo(height))
    }

}