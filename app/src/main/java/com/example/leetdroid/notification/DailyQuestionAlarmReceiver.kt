package com.example.leetdroid.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.leetdroid.utils.dialog.Notification

class DailyQuestionAlarmReceiver : BroadcastReceiver() {
    // TODO check what was the exception
    override fun onReceive(context: Context, intent: Intent) {
        try {
            Notification.showNotification(
                context,
                "Daily Challenge has been updated",
                "Lets complete this one too.",
                "daily_question",
                "daily_question_channel",
                103

            )
        } catch (ex: Exception) {
            Log.d("Receive Ex", "onReceive: ${ex.printStackTrace()}")
        }
    }
}











