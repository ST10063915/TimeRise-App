package com.opsc.timeriseprojectmain

import android.content.Context

object SettingsManager {
    private const val PREFS_NAME = "timerise_prefs"
    private const val MIN_HOURS = "min_hours"
    private const val MAX_HOURS = "max_hours"

    fun saveWorkHours(context: Context, minHours: Float, maxHours: Float) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putFloat(MIN_HOURS, minHours)
            putFloat(MAX_HOURS, maxHours)
            apply()
        }
    }

    fun getMinHours(context: Context): Float {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getFloat(MIN_HOURS, 0f) // Default to 0
    }

    fun getMaxHours(context: Context): Float {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getFloat(MAX_HOURS, 24f) // Default to 24
    }
}
