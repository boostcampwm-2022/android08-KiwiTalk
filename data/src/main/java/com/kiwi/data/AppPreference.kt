package com.kiwi.data

import android.content.SharedPreferences

class AppPreference constructor(private val prefs: SharedPreferences) {
    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue) ?: defValue
    }

    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}