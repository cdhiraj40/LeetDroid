package com.example.cdhiraj40

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.*

class UTCRule : TestWatcher() {
    private val origDefault: TimeZone = TimeZone.getDefault()
    override fun starting(description: Description) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    override fun finished(description: Description) {
        TimeZone.setDefault(origDefault)
    }
}