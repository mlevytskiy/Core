package com.core.core_adapters

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import wumf.com.appsprovider2.AppProvider

class AppsRecycleView(context: Context, attr: AttributeSet?) : RecyclerView(context, attr) {

    private val supervisor = SupervisorJob()
    private var scope = CoroutineScope(Dispatchers.IO + supervisor)
    private var appProvider: AppProvider? = null

    init {
        layoutManager = GridLayoutManager(getContext(), 3) as LayoutManager?

        var appsStr : String?
        var getAllAppsFromPhone = false

        context.theme.obtainStyledAttributes(
            attr,
            R.styleable.AppsRecycleView,
            0, 0).apply {
            try {
                appsStr = getString(R.styleable.AppsRecycleView_items)
                getAllAppsFromPhone = getBoolean(R.styleable.AppsRecycleView_getAllAppsFromPhone, false)
            } finally {
                recycle()
            }

        }
        appProvider = AppProvider(context)
        if (getAllAppsFromPhone) {
            setPackages("")
        }
    }

    fun setPackages(packagesStr: String?) {
        val packages = if (packagesStr?.isNotEmpty() ?: false) {
            packagesStr?.split(",") ?: emptyList()
        } else {
            emptyList()
        }
        startBgJob {
            var adapter: AppsAdapter? = null
            appProvider?.getNextApps(
                updateBlock = { apps ->
                    adapter?.notifyDataSetChanged() ?: run {
                        adapter = AppsAdapter(apps)
                        setAdapter(adapter)
                    }
                },
                packages = packages
            )
        }
    }

    private fun startBgJob(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(block = {
            block.invoke(this)
        })
    }

}