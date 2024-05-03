package com.opsc.timeriseprojectmain

import android.content.Context
import java.text.ParseException
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimesheetManager {
    private val timesheetEntries = mutableListOf<TimesheetEntry>()

    init {
        // For testing, ensure the dates are in the future
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1) // Add one day to ensure it's in the future
        addTimesheetEntry(calendar.time, "09:00", "17:00", "OPSC Frontend Dev", CategoryManager.getCategory(2)!!)
        addTimesheetEntry(calendar.time, "11:00", "12:00", "OPSC Assignment", CategoryManager.getCategory(2)!!)
        addTimesheetEntry(calendar.time, "11:00", "12:00", "OPSC Backend Dev", CategoryManager.getCategory(2)!!)
        addTimesheetEntry(calendar.time, "11:00", "12:00", "Meeting with Client", CategoryManager.getCategory(1)!!)
        addTimesheetEntry(calendar.time, "12:00", "13:00", "Meeting with CFO", CategoryManager.getCategory(1)!!)
        addTimesheetEntry(calendar.time, "17:00", "19:00", "Watch Netflix", CategoryManager.getCategory(3)!!)
        addTimesheetEntry(calendar.time, "20:00", "22:00", "Clean up apartment", CategoryManager.getCategory(3)!!)
    }

    fun addTimesheetEntry(date: Date, startTime: String, endTime: String, description: String, category: Category, photoPath: String? = null) {
        val newEntry = TimesheetEntry(timesheetEntries.size + 1, date, startTime, endTime, description, category, photoPath)
        timesheetEntries.add(newEntry)
    }

    fun getEntriesByCategory(categoryId: Int) = timesheetEntries.filter { it.category.id == categoryId }

    fun getSoonestEntries(): List<TimesheetEntry> {
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return timesheetEntries
            .filter { it.date.after(Date()) } // Filtering future entries
            .sortedBy {
                try {
                    dateTimeFormat.parse("${SimpleDateFormat("yyyy-MM-dd").format(it.date)} ${it.startTime}")
                } catch (e: ParseException) {
                    null
                }
            }
            .take(25)
    }

    fun calculateTotalHours(startDate: Date, endDate: Date, categoryId: Int): Float {
        return getEntriesByCategory(categoryId).filter { it.date in startDate..endDate }
            .sumByDouble {
                val start = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(it.startTime).time
                val end = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(it.endTime).time
                (end - start) / (1000 * 60 * 60).toDouble() // Convert milliseconds to hours
            }.toFloat()
    }

    fun getTotalHoursForDate(date: String): Float {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val targetDate = dateFormat.parse(date) ?: return 0f // Parse the input date string

        val totalHours = timesheetEntries
            .filter { dateFormat.format(it.date) == date } // Filter entries for the given date
            .sumByDouble {
                val start = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(it.startTime).time
                val end = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(it.endTime).time
                (end - start) / (1000 * 60 * 60).toDouble() // Convert milliseconds to hours
            }.toFloat()

        return totalHours
    }

    fun validateHours(context: Context, date: String, newHours: Float): Boolean {
        val totalHoursToday = TimesheetManager.getTotalHoursForDate(date) // Implement this method
        val minHours = SettingsManager.getMinHours(context)
        val maxHours = SettingsManager.getMaxHours(context)
        val newTotalHours = totalHoursToday + newHours
        return newTotalHours >= minHours && newTotalHours <= maxHours
    }
}
