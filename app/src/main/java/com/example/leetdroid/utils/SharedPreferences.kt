package com.example.leetdroid.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("data", Context.MODE_PRIVATE)

    var questionsFetched: Boolean
        get() = sharedPreferences.getBoolean("questionsFetched", false)
        set(value) = sharedPreferences.edit().putBoolean("questionsFetched", value).apply()

    var contestsInserted: Boolean
        get() = sharedPreferences.getBoolean("contestsInserted", false)
        set(value) = sharedPreferences.edit().putBoolean("contestsInserted", value).apply()

    var timerEnded: Boolean
        get() = sharedPreferences.getBoolean("timerEnded", false)
        set(value) = sharedPreferences.edit().putBoolean("timerEnded", value).apply()

    var userDataLoaded: Boolean
        get() = sharedPreferences.getBoolean("userDataLoaded", false)
        set(value) = sharedPreferences.edit().putBoolean("userDataLoaded", value).apply()

    var userAdded: Boolean
        get() = sharedPreferences.getBoolean("userAdded", false)
        set(value) = sharedPreferences.edit().putBoolean("userAdded", value).apply()

    var dailyQuestionLoaded: Boolean
        get() = sharedPreferences.getBoolean("dailyQuestionLoaded", false)
        set(value) = sharedPreferences.edit().putBoolean("dailyQuestionLoaded", value).apply()

    var dailyQuestionAdded: Boolean
        get() = sharedPreferences.getBoolean("dailyQuestionAdded", false)
        set(value) = sharedPreferences.edit().putBoolean("dailyQuestionAdded", value).apply()

    var firebaseUserAdded: Boolean
        get() = sharedPreferences.getBoolean("firebaseUserAdded", false)
        set(value) = sharedPreferences.edit().putBoolean("firebaseUserAdded", value).apply()

    var firebaseUserRegistered: Boolean
        get() = sharedPreferences.getBoolean("firebaseUserRegistered", false)
        set(value) = sharedPreferences.edit().putBoolean("firebaseUserRegistered", value).apply()

    var dayNotificationPushed: Boolean
        get() = sharedPreferences.getBoolean("dayNotificationPushed", false)
        set(value) = sharedPreferences.edit().putBoolean("dayNotificationPushed", value).apply()

    var minsNotificationPushed: Boolean
        get() = sharedPreferences.getBoolean("minsNotificationPushed", false)
        set(value) = sharedPreferences.edit().putBoolean("minsNotificationPushed", value).apply()

    var statusShown: Boolean
        get() = sharedPreferences.getBoolean("minsNotificationPushed", false)
        set(value) = sharedPreferences.edit().putBoolean("minsNotificationPushed", value).apply()

    fun removeAllPreferences() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.commit()
//        sharedPreferences.edit().clear().apply()
    }
}