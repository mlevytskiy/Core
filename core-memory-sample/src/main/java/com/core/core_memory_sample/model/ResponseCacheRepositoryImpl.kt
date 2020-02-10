package com.core.core_memory_sample.model

import android.content.Context
import android.util.Log
import io.objectbox.Box
import io.objectbox.kotlin.query
import java.util.*

class ResponseCacheRepositoryImpl(context: Context): ResponseCacheRepository {

    private val responseCacheBox: Box<ResponseCache>

    init {
        val boxStore = MyObjectBox.builder().androidContext(context).buildDefault()
        responseCacheBox = boxStore.boxFor(ResponseCache::class.java)
    }

    override fun set(date: Date?) {
        responseCacheBox.put(ResponseCache(date=date))
    }

    override fun get(): Date? {
        return responseCacheBox.query {
            equal(ResponseCache_.id, 1L)
        }.findFirst()?.date
    }

    override fun setObj(someObj: SomeObj?) {
        val responseCache = ResponseCache()
        responseCache.someObj.target = someObj
        Log.i("testr", "responseCacheBox.put(responseCache)")
        responseCacheBox.put(responseCache)
    }

    override fun getObj(): SomeObj? {
        return responseCacheBox.query {
            equal(ResponseCache_.id, 1L)
        }.findFirst()?.someObj?.target
    }

    override fun isEmpty() = responseCacheBox.isEmpty
}