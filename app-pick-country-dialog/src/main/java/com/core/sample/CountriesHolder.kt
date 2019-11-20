package com.core.sample

import android.content.res.AssetManager
import android.content.res.Resources
import android.text.TextUtils
import java.io.BufferedReader
import java.io.InputStreamReader

object CountriesHolder {

    val countries = ArrayList<Country>()

    fun syncLoad(assetManager: AssetManager, resources: Resources, packageName: String) {
        countries.clear()
        val reader = BufferedReader(InputStreamReader(assetManager.open("countries.csv")))
        for (item in reader.lineSequence()) {
            val value = TextUtils.split(item, ",")
            val telCode = Integer.parseInt(value[6])
            val code = value[3]
            val countryName = value[2]
            val flag = getFlag(code, resources, packageName)
            countries.add(Country(code, countryName, telCode, flag))
        }
    }

    private fun getFlag(code: String, res: Resources, packageName: String): Int {
        return res.getIdentifier("${if (code == "do") "does" else code.toLowerCase()}", "drawable", packageName)
    }


}