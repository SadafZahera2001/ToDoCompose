package com.example.to_docompose.data.models

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.to_docompose.data.ToDoDao

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}