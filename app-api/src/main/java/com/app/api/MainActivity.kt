package com.app.api

import com.app.api.api.WumfApi
import com.library.core.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity(R.layout.activity_main) {

    @Inject
    @JvmField
    var wumfApi : WumfApi? = null

    override fun getNavRes(): Int {
        return R.id.main_nav_host
    }

}
