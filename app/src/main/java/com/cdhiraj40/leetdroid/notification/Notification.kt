package com.cdhiraj40.leetdroid.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cdhiraj40.leetdroid.R
import com.cdhiraj40.leetdroid.ui.base.MainActivity

/**
 * Different Notification IDs
 * 101 -> Notification for a day before weekly contest
 * 102 -> Notification for a day before biweekly contest
 * 103 -> Notification for 30 minutes before weekly contest
 * 104 -> Notification for 30 minutes before biweekly contest
 * 105 -> Notification for new daily challenge
 */
object Notification {
    fun showNotification(
        context: Context,
        title: String,
        desc: String,
        channelId: String,
        channelName: String,
        notificationId: Int
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val random = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(context, random, intent, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            // TODO change icon later with app logo
            .setSmallIcon(R.drawable.ic_baseline_discuss_24)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(desc))
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, builder.build())
    }
}