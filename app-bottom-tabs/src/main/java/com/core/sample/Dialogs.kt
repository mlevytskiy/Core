package com.fortest.something.feature.onboarding

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import com.core.sample.R


fun showLanguageDialog(context: Context, arrayId: Int) {
    //
}


fun showSimpleDialog(context: Context, arrayId: Int, checkedItem: Int, select: (Array<String>, Int)->Unit, cancel: (DialogInterface)->Unit) {
    val array = context.resources.getStringArray(arrayId)
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