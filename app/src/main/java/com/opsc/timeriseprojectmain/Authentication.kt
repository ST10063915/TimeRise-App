package com.opsc.timeriseprojectmain

object Authentication {
    // Data eventually coming from the database
    private const val CORRECT_USERNAME = "admin"
    private const val CORRECT_PASSWORD = "123"
    private const val CORRECT_EMAIL = "admin@gmail.com"

    // Verify user login
    fun authenticate(username: String, password: String): Boolean {
        return username == CORRECT_USERNAME && password == CORRECT_PASSWORD
    }

    // Password reset
    fun reset_password(email: String): Boolean {
        return email == CORRECT_EMAIL
    }

    // Create account
    fun create_account(email: String, password: String, username: String, dateOfBirth: String): Boolean {
        return email == CORRECT_EMAIL && username == CORRECT_USERNAME && password == CORRECT_PASSWORD
    }
}