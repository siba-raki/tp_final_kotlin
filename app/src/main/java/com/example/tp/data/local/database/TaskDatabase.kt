package com.example.tp.data.local.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tp.data.local.dao.TaskDao
import com.example.tp.data.local.entity.TaskEntity
import com.example.tp.data.local.converters.LocalDateConverter

@Database(entities = [TaskEntity::class], version = 1)
@TypeConverters(LocalDateConverter::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "task_database"
    }
}