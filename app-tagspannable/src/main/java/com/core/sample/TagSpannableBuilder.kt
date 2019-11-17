package com.core.sample

import android.text.SpannableStringBuilder
import android.text.Spanned

class TagSpannableBuilder(str: String, private val textColor: Int, private val tagColor: Int) {

    private var stringBuilder: SpannableStringBuilder = SpannableStringBuilder(str)

    fun appendTag(tag: String): TagSpannableBuilder {
        stringBuilder.append("  " + tag + "  ")
        stringBuilder.setSpan(
            TagBadgeSpannable(textColor, tagColor),
            stringBuilder.length - ("  " + tag + "  ").length,
            stringBuilder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    fun build() = stringBuilder
}