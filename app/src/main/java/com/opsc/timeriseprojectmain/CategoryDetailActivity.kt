package com.opsc.timeriseprojectmain

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CategoryDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var timesheetAdapter: TimesheetAdapter
    private lateinit var addButton: Button
    private lateinit var startDateInput: EditText
    private lateinit var endDateInput: EditText
    private lateinit var totalHoursText: TextView
    private var categoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_detail)

        categoryId = intent.getIntExtra("CATEGORY_ID", -1)
        recyclerView = findViewById(R.id.recycler_view_timesheet_entries)
        recyclerView.layoutManager = LinearLayoutManager(this)
        timesheetAdapter = TimesheetAdapter(TimesheetManager.getEntriesByCategory(categoryId))
        recyclerView.adapter = timesheetAdapter

        addButton = findViewById(R.id.button_add_time_entry)
        addButton.setOnClickListener {
            showAddTimeEntryDialog(categoryId)
        }

        startDateInput = findViewById(R.id.startDateInput)
        endDateInput = findViewById(R.id.endDateInput)
        totalHoursText = findViewById(R.id.totalHoursText)

        startDateInput.setOnClickListener {
            showDatePickerDialog(startDateInput)
        }

        endDateInput.setOnClickListener {
            showDatePickerDialog(endDateInput)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showDatePickerDialog(dateInput: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            dateInput.setText(dateFormatter.format(selectedDate.time))
            updateTotalHours()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun updateTotalHours() {
        val startDateStr = startDateInput.text.toString()
        val endDateStr = endDateInput.text.toString()

        if (startDateStr.isNotEmpty() && endDateStr.isNotEmpty()) {
            try {
                val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(startDateStr)
                val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(endDateStr)
                if (startDate != null && endDate != null && !startDate.after(endDate)) {
                    val totalHours = TimesheetManager.calculateTotalHours(startDate, endDate, categoryId)
                    totalHoursText.text = "Total Hours: $totalHours"
                } else {
                    totalHoursText.text = "Invalid date range"
                }
            } catch (e: ParseException) {
                totalHoursText.text = "Error parsing dates"
            }
        } else {
            totalHoursText.text = "Please select both start and end dates"
        }
    }


    private fun showAddTimeEntryDialog(categoryId: Int) {
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.dialog_add_time_entry, null)
        val editTextDescription = view.findViewById<EditText>(R.id.editText_description)
        val editTextDate = view.findViewById<EditText>(R.id.editText_date)
        val editTextStartTime = view.findViewById<EditText>(R.id.editText_start_time)
        val editTextEndTime = view.findViewById<EditText>(R.id.editText_end_time)

        MaterialAlertDialogBuilder(this)
            .setTitle("Add New Time Entry")
            .setView(view)
            .setPositiveButton("Add") { dialog, _ ->
                val description = editTextDescription.text.toString().trim()
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(editTextDate.text.toString())
                if (date != null && description.isNotEmpty()) {
                    TimesheetManager.addTimesheetEntry(date, editTextStartTime.text.toString(), editTextEndTime.text.toString(), description, CategoryManager.getCategory(categoryId)!!)
                    timesheetAdapter.timesheetEntries = TimesheetManager.getEntriesByCategory(categoryId) // Update the adapter's data source
                    timesheetAdapter.notifyItemInserted(timesheetAdapter.timesheetEntries.size - 1) // Notify the adapter
                    recyclerView.scrollToPosition(timesheetAdapter.timesheetEntries.size - 1) // Optionally scroll to the new item
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
    class TimesheetAdapter(var timesheetEntries: List<TimesheetEntry>) :
        RecyclerView.Adapter<TimesheetAdapter.TimesheetViewHolder>() {

        class TimesheetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val dateTextView: TextView = view.findViewById(R.id.text_date)
            val startTimeTextView: TextView = view.findViewById(R.id.text_start_time)
            val endTimeTextView: TextView = view.findViewById(R.id.text_end_time)
            val descriptionTextView: TextView = view.findViewById(R.id.text_description)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimesheetViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timesheet_entry, parent, false)
            return TimesheetViewHolder(view)
        }

        override fun onBindViewHolder(holder: TimesheetViewHolder, position: Int) {
            val timesheetEntry = timesheetEntries[position]
            holder.dateTextView.text = formatDate(timesheetEntry.date)
            holder.startTimeTextView.text = timesheetEntry.startTime
            holder.endTimeTextView.text = timesheetEntry.endTime
            holder.descriptionTextView.text = timesheetEntry.description
        }

        override fun getItemCount(): Int = timesheetEntries.size

        private fun formatDate(date: Date): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return formatter.format(date)
        }
    }
}
