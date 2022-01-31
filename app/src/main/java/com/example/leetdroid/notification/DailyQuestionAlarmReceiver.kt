package com.example.leetdroid.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.leetdroid.R
import com.example.leetdroid.ui.base.MainActivity

class DailyQuestionAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            showNotification(
                context,
                "Daily Challenge has been updated",
                "Lets complete this one too."
            )
        } catch (ex: Exception) {
            Log.d("Receive Ex", "onReceive: ${ex.printStackTrace()}")
        }
    }
}

private fun showNotification(context: Context, title: String, desc: String) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "daily_question_channel"
    val channelName = "daily_question"

    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        // TODO change icon later with app logo
        .setSmallIcon(R.drawable.ic_baseline_discuss_24)
        .setContentTitle(title)
        .setContentText(desc)
        .setAutoCancel(true)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent);

    notificationManager.notify(1, builder.build())
}











