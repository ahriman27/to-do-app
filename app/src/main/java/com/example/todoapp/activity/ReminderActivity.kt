package com.example.todoapp.activity

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.todoapp.Database.DbHelper
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityReminderBinding
import com.example.todoapp.model.Model
import com.example.todoapp.other.Alarm
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReminderActivity : AppCompatActivity() {

    lateinit var binding: ActivityReminderBinding
    var timeToNotify = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reminder)

        binding.apply {

            btnTime.setOnClickListener {
                selectTime()
            }

            btnDate.setOnClickListener {
                selectDate()
            }

            btnSbumit.setOnClickListener {

                val title: String = editTitle.text.toString()
                    .trim { it <= ' ' }

                val date: String = btnDate.text.toString()
                    .trim { it <= ' ' }

                val time: String = btnTime.text.toString()
                    .trim { it <= ' ' }

                if (title.isEmpty()) {
                    Toast.makeText(applicationContext, "Please Enter text", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (time == "time" || date == "date") {
                        Toast.makeText(
                            applicationContext,
                            "Please select date and time",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        processinsert(title, date, time)
                    }
                }
            }
        }
    }

    private fun processinsert(title: String, date: String, time: String) {
        val result: String? = DbHelper(this).addReminder(
            title,
            date,
            time
        )

        if(result == "Successfully inserted"){
            val cursor: Cursor? =
                DbHelper(applicationContext).readAllReminders()
            var id = 2
            while (cursor!!.moveToNext()) {
                id = cursor.getInt(0)
            }
            setAlarm(title, date, time,id)
        }


        binding.editTitle.setText("")
        Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
    }

    private fun selectTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            this,
            { timePicker, i, i1 ->
                timeToNotify = "$i:$i1"
                binding.btnTime.setText(FormatTime(i, i1))
            }, hour, minute, false
        )
        timePickerDialog.show()
    }

    private fun selectDate() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            this,
            { datePicker, year, month, day ->
                binding.btnDate.setText(day.toString() + "-" + (month + 1) + "-" + year)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    fun FormatTime(
        hour: Int,
        minute: Int
    ): String? {
        var time: String
        time = ""
        val formattedMinute: String
        formattedMinute = if (minute / 10 == 0) {
            "0$minute"
        } else {
            "" + minute
        }
        time = if (hour == 0) {
            "12:$formattedMinute AM"
        } else if (hour < 12) {
            "$hour:$formattedMinute AM"
        } else if (hour == 12) {
            "12:$formattedMinute PM"
        } else {
            val temp = hour - 12
            "$temp:$formattedMinute PM"
        }
        return time
    }

    private fun setAlarm(text: String, date: String, time: String,id : Int) {
        val am = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, Alarm::class.java)

        intent.putExtra(
            "event",
            text
        )

        intent.putExtra("time", date)
        intent.putExtra("date", time)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, id, intent, PendingIntent.FLAG_ONE_SHOT)
        val dateandtime = "$date $timeToNotify"
        val formatter: DateFormat = SimpleDateFormat("d-M-yyyy hh:mm")
        try {
            val date1 = formatter.parse(dateandtime)
            am[AlarmManager.RTC_WAKEUP, date1.time - 5*60*1000] = pendingIntent
            Toast.makeText(applicationContext, "Alarm", Toast.LENGTH_SHORT).show()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val intentBack = Intent(
            applicationContext,
            MainActivity::class.java
        )
        intentBack.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intentBack)
    }

}