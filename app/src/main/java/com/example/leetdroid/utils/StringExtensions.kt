package com.example.leetdroid.utils

import android.text.SpannableString
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan

object StringExtensions {
    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    fun String.setFontSizeForPath(
        path: String,
        fontSizeInPixel: Int,
        colorCode: String = "#FF0000"
    ): SpannableString {
        val spannable = SpannableString(this)
        val startIndexOfPath = spannable.toString().indexOf(path)
        spannable.setSpan(
            AbsoluteSizeSpan(fontSizeInPixel),
            startIndexOfPath,
            startIndexOfPath + path.length,
            0
        )
        spannable.setSpan(
            AbsoluteSizeSpan(fontSizeInPixel),
            startIndexOfPath,
            startIndexOfPath + path.length,
            0
        )

        return spannable
    }
}