package com.core.sample

import com.library.core.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {

    override fun getNavRes(): Int {
        return R.id.main_nav_host
    }

}
