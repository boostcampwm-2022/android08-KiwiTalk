package com.kiwi.kiwitalk

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreference @Inject constructor(private val prefs: SharedPreferences) {
    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue) ?: defValue
    }

    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}