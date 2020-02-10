package com.core.sample

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.core.sample.memory.UserInfoRepository
import com.core.sample.util.LocaleHelper
import com.library.core.BaseActivity


abstract class WumfBaseActivity(layoutRes: Int) : BaseActivity(layoutRes) {

    protected lateinit var repository: UserInfoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        repository = (application as App).repository!!
        val language = getLanguage(repository.getLanguage().toInt())
        LocaleHelper.setLocale(this, language)
        super.onCreate(savedInstanceState)
    }

    open fun getLanguage(number: Int): String {
        var str  = "en"
        if (number == 0) {
            str  = "en"
        } else if (number == 1) {
            str  = "ru"
        }
        return str
    }



}