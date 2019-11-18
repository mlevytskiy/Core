package com.core.core_memory_sample

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.core.core_memory_sample.model.*
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity: Activity() {

    private val supervisor = SupervisorJob()
    private var scope = CoroutineScope(Dispatchers.IO + supervisor)

    private var systemInfoRepository: SystemInfoRepository? = null
    private var responseCacheRepository: ResponseCacheRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        systemInfoRepository = SystemInfoRepositoryImpl(this)
        responseCacheRepository = ResponseCacheRepositoryImpl(this)

    }

    fun onClickSave(view: View) {
//        systemInfoRepository?.let {
//            val randomV = Random(100).nextInt()
//            val diff = getFormatTimeDiff(calculateTimeDiff { it.set(randomV) })
//            Toast.makeText(this, "value $randomV saved; time: $diff", Toast.LENGTH_LONG).show()
//        }
        responseCacheRepository?.let {
            it.set(12, Date())
        }
    }

    fun onClickLoad(view: View) {
        responseCacheRepository?.let {
            val isEmpty = it.get(12) == null
            Toast.makeText(this, "isEmpty=$isEmpty", Toast.LENGTH_LONG).show()
        }
//        systemInfoRepository?.let {
//            if (it.isEmpty()) {
//                Toast.makeText(this, "repository is empty", Toast.LENGTH_LONG).show()
//            } else {
//                var value = 0
//                val diff = getFormatTimeDiff(calculateTimeDiff { value = it.get() })
//                Toast.makeText(this, "$value in time: $diff", Toast.LENGTH_LONG).show()
//            }
//        }
    }

    fun startBgJob(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(block = {
            block.invoke(this)
        })
    }

    suspend fun <T> runMain(block: suspend CoroutineScope.() -> T): T =
        withContext(Dispatchers.Main, block)

    fun calculateTimeDiff(block: ()-> Unit): Long {
        val time1 = System.currentTimeMillis()
        block()
        val time2 = System.currentTimeMillis()
        return time2-time1
    }

    fun getFormatTimeDiff(diff: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        val ms = diff - seconds*1000
        return "$seconds seconds $ms ms"
    }

}