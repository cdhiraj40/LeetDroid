package com.cdhiraj40.leetdroid.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.cdhiraj40.leetdroid.notification.DailyQuestionAlarmReceiver
import com.cdhiraj40.leetdroid.notification.DayContestAlarmReceiver
import com.cdhiraj40.leetdroid.notification.MinContestAlarmReceiver
import com.cdhiraj40.leetdroid.ui.fragments.HomeFragment
import java.util.*

/**
 * Different requestCodes
 * 0 -> RequestCode for a daily Question Notifications
 * 1 -> RequestCode for a day before Notifications
 * 2 -> RequestCode for 30 mins before Notifications
 */
object AlarmUtils {
    fun setAlarm(context: Context) {
        val cal = Calendar.getInstance()

        // TODO change time of compared calendar as checking and showing notification around only 5.00 AM is useless
        /**
         * leetcode daily challenges gets updated 00:00 UTC everyday
         * that is 5.30 am in IST
         * we are gonna send notification about it every day 5 am of device's time
         */
        cal.set(Calendar.HOUR_OF_DAY, 5)
        cal.set(Calendar.MINUTE, 0)

        // send notification every day for a new daily question
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, DailyQuestionAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

        if (Calendar.getInstance().after(cal)) {
            cal.add(Calendar.DATE, 1)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Log.d(
            Constant.TAG(HomeFragment::class.java).toString(),
            "alarm for daily questions has been set"
        )
    }

    /**
     * setting alarm for a day before contest.
     */
    fun setDayAlarm(context: Context, startingTime: Long, contestName: String) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startingTime

        // send notification every day for a new daily question
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(
            context,
            DayContestAlarmReceiver::class.java
        )
        DayContestAlarmReceiver().alarmName = contestName
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        Log.d(
            Constant.TAG(HomeFragment::class.java).toString(),
            "alarm for a day before contest has been set"
        )
    }

    /**
     * setting alarm for 30 mins before contest.
     */
    fun set30MinsAlarm(context: Context, startingTime: Long, contestName: String) {
        val calendar = Calendar.getInstance()
        // 1.8e+6 is 30 mins in milli seconds
        calendar.timeInMillis = (startingTime - 1.8e+6).toLong()

        // send notification every day for a new daily question
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(
            context,
            MinContestAlarmReceiver::class.java
        )
        MinContestAlarmReceiver().alarmName = contestName
        val pendingIntent = PendingIntent.getBroadcast(context, 2, alarmIntent, 0)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        Log.d(
            Constant.TAG(HomeFragment::class.java).toString(),
            "alarm for 30 mins before contest has been set"
        )
    }
}