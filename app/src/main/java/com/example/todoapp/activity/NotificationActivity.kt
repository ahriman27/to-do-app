package com.example.todoapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    lateinit var binding : ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification)

        binding.apply {
            val bundle = intent.extras
            tvMessage.text = (bundle!!.getString("message"))
        }

    }
}