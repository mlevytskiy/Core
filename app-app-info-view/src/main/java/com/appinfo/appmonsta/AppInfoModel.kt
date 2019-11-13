package com.appinfo.appmonsta

import android.graphics.drawable.Drawable

data class AppInfoModel(val appName: String, val packageName: String, val imageUrl: String)

data class AppInfoModelFromPhone(val appName: String, val packageName: String, val pathToIconFile: String)