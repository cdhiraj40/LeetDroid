package com.example.leetdroid.utils

import android.text.TextUtils

object StringExtensions {
    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }
}