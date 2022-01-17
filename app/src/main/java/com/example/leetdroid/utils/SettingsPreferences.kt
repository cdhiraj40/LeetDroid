package com.example.gdsc_hackathon.utils

import android.app.Application
import androidx.appcompat.app.AppCompatActivity

object SettingsPreferences {

    val PREFERENCES = "preferences"
    val CUSTOM_THEME = "customTheme"
    val LIGHT_THEME = "lightTheme"
    val DARK_THEME = "darkTheme"

    private var customTheme: String? = null

    fun getCustomTheme(): String? {
        return customTheme
    }

    fun setCustomTheme(customTheme: String?) {
        this.customTheme = customTheme
    }
}