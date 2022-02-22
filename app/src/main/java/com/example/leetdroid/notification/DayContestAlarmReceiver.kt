package com.example.leetdroid.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DayContestAlarmReceiver : BroadcastReceiver() {
    // TODO check what was the exception
    var alarmName: String? = null
    override fun onReceive(context: Context, intent: Intent) {
        try {
            Notification.showNotification(
                context,
                alarmName!!,
                "There is only a day remaining to $alarmName register now!",
                "contest_reminder_1day_weekly",
                "contest_reminder_1day_weekly_channel",
                101
            )

        } catch (ex: Exception) {
            Log.d("Receive Ex", "onReceive: ${ex.printStackTrace()}")
        }
    }
}











