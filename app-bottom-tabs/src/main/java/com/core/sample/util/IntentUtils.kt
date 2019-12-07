package com.core.sample.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun showInGooglePlay(pkg: String, context: Context) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg")))
    } catch (anfe: android.content.ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$pkg")
            )
        )
    }

}