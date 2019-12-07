package com.core.core_adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import wumf.com.appsprovider2.AppContainer

class AppsAdapter(val apps: MutableList<AppContainer> = ArrayList(),
                  private val likes: Map<String, Int>,
                  private var listenerBlock: ((AppContainer, Int)->Unit)? ) : RecyclerView.Adapter<AppViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(parent, likes)
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(apps[position], listenerBlock)
    }

    fun setItemClick(block: (AppContainer, Int)->Unit ) {
        listenerBlock = block
    }

}