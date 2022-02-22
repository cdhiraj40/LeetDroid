package com.example.leetdroid.utils

import android.text.format.DateFormat
import org.threeten.bp.DateTimeUtils.toDate
import org.threeten.bp.Instant
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalAccessor
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun parseISO8601Date(date: String): Date {
        val temporalAccessor: TemporalAccessor = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(date)
        val instant = Instant.from(temporalAccessor)
        return formatISO8601Date(toDate(instant))
    }

    fun formatISO8601Date(date: Date): Date {
        val formatter = SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.getDefault())
        return formatter.parse(date.toString())!!
    }

    fun getDate(date: Date): String? {
        return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
    }

    fun getTime(date: Date?): String? {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date!!)
    }

    fun getHours(time: String): String {
        return (time.toInt() / 3600.0).toString()
    }

    fun getSeconds(time: String): Int {
        val actualTime = time.split(':')
        return actualTime[0].toInt() * 60 * 60 + (actualTime[1]).toInt() * 60
    }

    fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    }

    fun convertDateFromMill(mill: Long): String {
        return DateFormat.format("dd-MMM-yyyy", mill * 1000).toString()
    }

    fun convertTimeFomMill(mill: Long): String {
        return DateFormat.format("hh:mm a", mill * 1000).toString()
    }

    fun convertDateTimeFomMill(mill: Long): String {
        return DateFormat.format("dd-MMM-yyyy hh:mm a", mill * 10).toString()
    }
}