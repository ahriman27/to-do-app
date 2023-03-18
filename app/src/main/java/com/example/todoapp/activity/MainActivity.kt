package com.example.todoapp.activity

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.Database.DbHelper
import com.example.todoapp.R
import com.example.todoapp.adapter.TaskAdapter
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.model.Model

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    var dataholder = ArrayList<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.apply {
            addBtn.setOnClickListener {
                val intent = Intent(applicationContext, ReminderActivity::class.java)
                startActivity(intent)
            }
        }

        val cursor: Cursor? =
            DbHelper(applicationContext).readAllReminders()

        while (cursor!!.moveToNext()) {
            val model = Model(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3))
            dataholder.add(model)
        }

        binding.taskRecycler.layoutManager = LinearLayoutManager(this)
        binding.taskRecycler.adapter = TaskAdapter(this,dataholder)

    }
}