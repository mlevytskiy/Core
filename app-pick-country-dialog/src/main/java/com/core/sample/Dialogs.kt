package com.fortest.something.feature.onboarding

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.core.sample.CountriesAdapter
import com.core.sample.Country
import com.core.sample.R

fun showCountriesDialog(context: Context, countries: ArrayList<Country>, checkedItem: Int, select: (Country)->Unit, cancel: (DialogInterface)->Unit): DialogInterface {
    return AlertDialog.Builder(context)
        .setTitle("")
        .setView(createCountriesView(context, countries, select))
        .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                cancel(dialog)
            }
        }).show()
}
private fun createCountriesView(context: Context, countries: ArrayList<Country>, select: (Country)->Unit): View {
    val li = LayoutInflater.from(context)
    val listView = li.inflate(R.layout.dialog_countries, null) as ListView
    val gray = context.resources.getColor(R.color.observatory_alpha_6)
    val adapter = CountriesAdapter(countries, Color.WHITE, gray)
    listView.adapter = adapter
    listView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            select(countries[position])
        }

    })
    return listView
}
