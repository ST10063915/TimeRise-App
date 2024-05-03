package com.opsc.timeriseprojectmain

import android.net.Uri

object CategoryManager {
    private val categories = mutableListOf<Category>()

    init {
        // Adding some sample categories
        addCategory("Work", Uri.parse("android.resource://com.opsc.timeriseprojectmain/drawable/screenshot_2024_04_28_191523"))
        addCategory("School", Uri.parse("android.resource://com.opsc.timeriseprojectmain/drawable/screenshot_2024_04_28_191523"))
        addCategory("Free Time", Uri.parse("android.resource://com.opsc.timeriseprojectmain/drawable/screenshot_2024_04_28_191523"))
    }

    fun addCategory(name: String, imageUri: Uri? = null): Category {
        val newCategory = Category(categories.size + 1, name, imageUri ?: Uri.EMPTY)
        categories.add(newCategory)
        return newCategory
    }

    fun getCategory(id: Int) = categories.find { it.id == id }
    fun getAllCategories() = categories.toList()
}
