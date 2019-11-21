package com.core.sample.util

import android.text.Spannable
import androidx.databinding.ObservableField
import com.core.sample.R
import com.core.sample.TagSpannableBuilder

class HomeTitle(private val strRes: StringRes, colorRes: ColorRes): ObservableField<Spannable>() {

    var countryName: String = "Ukraine"

    var type: Type = Type.IN_THE_WORLD
        set(value) {
            field = value
            set(buildSpannable(strPrefix, getTagStr(strRes, value)))
        }

    private val strPrefix = strRes.getStr(R.string.find_the_best_apps_in_the_world)
    private val textColor = colorRes.getColor(android.R.color.white)
    private val tagBgColor = colorRes.getColor(R.color.finn_alpha_69)

    init {
        set(buildSpannable(strPrefix, getTagStr(strRes, type)))
    }

    fun forciblyTextUpdate() {
        set(buildSpannable(strPrefix, getTagStr(strRes, type)))
    }

    private fun getTagStr(strRes: StringRes, type: Type) = when (type) {
        Type.IN_THE_WORLD -> strRes.getStrFromArray(R.array.type_of_apps, 0)
        Type.IN_COUNTRY -> String.format(strRes.getStrFromArray(R.array.type_of_apps, 1), countryName)
        Type.AMONG_FRIENDS -> strRes.getStrFromArray(R.array.type_of_apps, 3)
    }

    private fun buildSpannable(strPrefix: String, tag: String): Spannable {
        return TagSpannableBuilder(strPrefix, textColor, tagBgColor).appendTag(tag).build()
    }

    enum class Type {
        IN_THE_WORLD,
        IN_COUNTRY,
        AMONG_FRIENDS
    }

}