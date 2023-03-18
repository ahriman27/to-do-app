package com.example.todoapp.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todoapp.other.Constants

class DbHelper(context : Context) : SQLiteOpenHelper(context,Constants.DATABASE_NAME,null,1)
{

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "create table tbl_reminder(id integer primary key autoincrement,title text,date text,time text)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val query = "DROP TABLE IF EXISTS tbl_reminder"

        db?.execSQL(query)
        onCreate(db)
    }

    fun addReminder(title: String?, date: String?, time: String?): String? {
        val database = this.readableDatabase
        val contentValues = ContentValues()
        contentValues.put("title", title)
        contentValues.put("date", date)
        contentValues.put("time", time)
        val result = database.insert("tbl_reminder", null, contentValues)
            .toFloat()
        return if (result == -1f) {
            "Failed"
        } else {
            "Successfully inserted"
        }
    }

    fun deleteRow(value: Int) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM tbl_reminder WHERE id ='$value'")
        db.close()
    }


    fun readAllReminders(): Cursor? {
        val database = this.writableDatabase
        val query =
            "select * from tbl_reminder order by id desc"
        return database.rawQuery(query, null)
    }

}