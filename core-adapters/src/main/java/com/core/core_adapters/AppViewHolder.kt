package com.core.core_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appinfo.appmonsta.AppInfoView
import wumf.com.appsprovider2.AppContainer

class AppViewHolder(parent: ViewGroup):
    RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false)) {

    fun bind(app: AppContainer) {
        (itemView as AppInfoView).setModel(app)
    }

}