package com.core.app_appsview_sample

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.core.core_adapters.AppsRecycleView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickSend(view: View) {
        val appsView:AppsRecycleView = findViewById<View>(R.id.apps_recycle_view) as AppsRecycleView
        val packagesView = findViewById<EditText>(R.id.packages)
        val sendButton = view

        val packages = packagesView.text.toString()
        appsView.setPackages(packages)
        appsView.visibility= RecyclerView.VISIBLE

        sendButton.visibility = GONE
        packagesView.visibility = GONE
    }

}