package com.opsc.timeriseprojectmain

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val delayMillis: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Display loading text
        val loadingText: TextView = findViewById(R.id.loadingText)
        loadingText.text = "Loading..."

        // Delay and redirect to sign-in page
        Handler().postDelayed({
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
            finish() // Close the loading page
        }, delayMillis)
    }
}