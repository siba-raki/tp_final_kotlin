package com.example.tp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String? = null,
    val category: String,
    val isCompleted: Boolean = false,
    val createdAt: LocalDate = LocalDate.now(),
    val dueDate: LocalDate? = null
)