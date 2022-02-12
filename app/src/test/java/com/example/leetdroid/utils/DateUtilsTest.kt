package com.example.leetdroid.utils

import com.example.leetdroid.utils.DateUtils.formatISO8601Date
import com.example.leetdroid.utils.DateUtils.getDate
import com.example.leetdroid.utils.DateUtils.getHours
import com.example.leetdroid.utils.DateUtils.getSeconds
import com.example.leetdroid.utils.DateUtils.getTime
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalAccessor
import java.util.*

@RunWith(JUnit4::class)
class DateUtilsTest {

    private val dateISO8601 = "2022-02-19T14:30:00.000Z"

    @Test
    fun parseISO8601DateTest() {
        val result = parseISO8601Date(dateISO8601)
        val expected = "Sat Feb 19 20:00:00 IST 2022"
        assertThat(result.toString()).isEqualTo(expected)
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
        val result = getDate(DateUtils.parseISO8601Date(dateISO8601))
        val expected = "Feb 19, 2022"
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun getTimeTest() {
        val result = getTime(DateUtils.parseISO8601Date(dateISO8601))
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
        val temporalAccessor: TemporalAccessor = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(date)
        val instant = Instant.from(temporalAccessor)
        return DateTimeUtils.toDate(instant)
    }
}