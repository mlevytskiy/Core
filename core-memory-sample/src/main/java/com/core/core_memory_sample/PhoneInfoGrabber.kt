package com.core.core_memory_sample

import android.content.Context
import android.content.pm.ResolveInfo
import android.content.Intent
import java.io.File.separator


object PhoneInfoGrabber {

    fun getApps(context: Context): String {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkgAppsList = context.packageManager.queryIntentActivities(mainIntent, 0)
        val apps = pkgAppsList.map { resolveInfo -> resolveInfo.activityInfo.packageName }.joinToString(separator=",")
        return apps
    }

}