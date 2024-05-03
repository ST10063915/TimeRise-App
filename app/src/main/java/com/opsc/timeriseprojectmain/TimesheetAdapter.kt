package com.opsc.timeriseprojectmain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.Locale


class TimesheetAdapter(private val timesheetEntries: List<TimesheetEntry>) :
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

        // Format the date
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        holder.dateTextView.text = formatter.format(timesheetEntry.date) // Formatting date to String

        holder.startTimeTextView.text = timesheetEntry.startTime
        holder.endTimeTextView.text = timesheetEntry.endTime
        holder.descriptionTextView.text = timesheetEntry.description

        // Asynchronously load the image

    }

    override fun getItemCount() = timesheetEntries.size

    private class ImageLoaderTask(imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        private val imageViewReference: WeakReference<ImageView> = WeakReference(imageView)

        override fun doInBackground(vararg params: String): Bitmap? {
            return BitmapFactory.decodeFile(params[0])  // Assuming the imageUrl is a direct file path
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            bitmap?.let {
                val imageView = imageViewReference.get()
                imageView?.setImageBitmap(bitmap)
            }
        }
    }
}

