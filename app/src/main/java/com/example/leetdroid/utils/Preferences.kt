package com.example.leetdroid.utils

import android.content.Context
import android.content.SharedPreferences


class Preferences(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("data", Context.MODE_PRIVATE)

    var questionsFetched: Boolean
        get() = preferences.getBoolean("questionsFetched", false)
        set(value) = preferences.edit().putBoolean("questionsFetched", value).apply()

    var contestsInserted: Boolean
        get() = preferences.getBoolean("contestsInserted", false)
        set(value) = preferences.edit().putBoolean("contestsInserted", value).apply()
}