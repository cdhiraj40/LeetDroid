package com.example.leetdroid.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MinContestAlarmReceiver : BroadcastReceiver() {
    // TODO check what was the exception
    var alarmName: String? = null
    override fun onReceive(context: Context, intent: Intent) {
        try {
            Notification.showNotification(
                context,
                alarmName!!,
                "There is only 30 mins remaining to $alarmName register now!",
                "contest_reminder_30_mins_biweekly",
                "contest_reminder_30_mins_biweekly_channel",
                104
            )
        } catch (ex: Exception) {
            Log.d("Receive Ex", "onReceive: ${ex.printStackTrace()}")
        }
    }
}











