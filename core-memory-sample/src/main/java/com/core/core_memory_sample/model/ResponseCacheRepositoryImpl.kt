package com.core.core_memory_sample.model

import android.content.Context
import io.objectbox.Box
import io.objectbox.kotlin.query
import java.util.*

class ResponseCacheRepositoryImpl(context: Context): ResponseCacheRepository {

    private val responseCacheBox: Box<ResponseCache>

    init {
        val boxStore = MyObjectBox.builder().androidContext(context).buildDefault()
        responseCacheBox = boxStore.boxFor(ResponseCache::class.java)
    }

    override fun set(id: Long, date: Date?) {
        responseCacheBox.put(ResponseCache(id, date))
    }

    override fun get(id: Long): Date? {
        return responseCacheBox.query {
            equal(ResponseCache_.id, id)
        }.findFirst()?.date
    }

    override fun isEmpty() = responseCacheBox.isEmpty
}