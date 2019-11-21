package com.fortest.something.feature.onboarding

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.core.sample.R
import com.core.sample.util.countriesdialog.CountriesAdapter
import com.core.sample.util.countriesdialog.Country

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

fun showLanguageDialog(context: Context, arrayId: Int) {
    //
}


fun showSimpleDialog(context: Context, arrayId: Int, checkedItem: Int, select: (Array<String>, Int)->Unit, cancel: (DialogInterface)->Unit,
                     defCountryName: String ) {
    val array = context.resources.getStringArray(arrayId)
    array[1] = String.format(array[1], defCountryName)
    AlertDialog.Builder(context)
        .setTitle("")
        .setSingleChoiceItems(array, checkedItem, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, position: Int) {
                dialog?.dismiss()
                select(array, position)
            }
        })
        .setNegativeButton(R.string.cancel, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                cancel(dialog)
            }
        })
        .show()
}