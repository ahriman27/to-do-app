package com.example.todoapp.adapter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Database.DbHelper
import com.example.todoapp.databinding.ItemTaskBinding
import com.example.todoapp.model.Model
import com.example.todoapp.other.Alarm

class TaskAdapter(var context : Context,var list : ArrayList<Model>) : RecyclerView.Adapter<TaskAdapter.ViewHold>()
{

    class ViewHold(bind : ItemTaskBinding) : RecyclerView.ViewHolder(bind.root){
        var binding = bind
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        return ViewHold(ItemTaskBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {

        holder.binding.apply {

            txtTitle.text = list[position].title
            txtTime.text = list[position].time
            txtDate.text = list[position].date

            delete.setOnClickListener {
                DbHelper(context).deleteRow(list[position].id)
                list.removeAt(position)

                try {
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
                    val myIntent = Intent(
                        context,
                        Alarm::class.java
                    )
                    val pendingIntent = PendingIntent.getBroadcast(
                        context, list[position].id, myIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    alarmManager?.cancel(pendingIntent)
                }
                catch (e : Exception){

                }

                notifyDataSetChanged()
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}