package com.opsc.timeriseprojectmain

import java.util.Date

data class TimesheetEntry(
    val id: Int,
    val date: Date,
    val startTime: String,
    val endTime: String,
    val description: String,
    val category: Category,
    var imageUrl: String? = null
)
