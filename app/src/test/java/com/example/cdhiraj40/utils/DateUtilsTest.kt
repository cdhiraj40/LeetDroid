package com.example.cdhiraj40.utils

import com.cdhiraj40.leetdroid.utils.DateUtils.formatISO8601Date
import com.cdhiraj40.leetdroid.utils.DateUtils.getDate
import com.cdhiraj40.leetdroid.utils.DateUtils.getHours
import com.cdhiraj40.leetdroid.utils.DateUtils.getSeconds
import com.cdhiraj40.leetdroid.utils.DateUtils.getTime
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RunWith(JUnit4::class)
class DateUtilsTest {

    private val dateISO8601 = "2022-02-19T14:30:00.000Z"

    @Test
    fun parseISO8601DateTest() {
        val result = parseISO8601Date(dateISO8601).toString()
        val expected = "Sat Feb 19 20:00:00 IST 2022"
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun formatISO8601DateTest() {
        val instant = parseISO8601Date(dateISO8601)
        val result = formatISO8601Date(instant)
        val expected = "Sat Feb 19 20:00:00 IST 2022"
        assertThat(result.toString()).isEqualTo(expected)
    }

    @Test
    fun getDateTest() {
        val result = getDate(parseISO8601Date(dateISO8601))
        val expected = "Feb 19, 2022"
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun getTimeTest() {
        val result = getTime(parseISO8601Date(dateISO8601))
        val expected = "20:00"
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun getHoursTest() {
        val result = getHours("5400")
        val expected = "1.5"
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun getSecondsTest() {
        val result = getSeconds("00:01")
        val expected = "60"
        assertThat(result.toString()).isEqualTo(expected)
    }

    private fun parseISO8601Date(date: String): Date {
        val timeFormatter = DateTimeFormatter.ISO_DATE_TIME
        val offsetDateTime: OffsetDateTime =
            OffsetDateTime.parse(date, timeFormatter)

        return Date.from(Instant.from(offsetDateTime))
    }
}
