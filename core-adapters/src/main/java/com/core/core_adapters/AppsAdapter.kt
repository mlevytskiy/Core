package com.core.core_adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import wumf.com.appsprovider2.AppContainer

class AppsAdapter(val apps: List<AppContainer> = ArrayList()) : RecyclerView.Adapter<AppViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(apps[position])
    }

}