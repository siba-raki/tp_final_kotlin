package com.example.tp.ui.addedit


import com.example.tp.data.local.entity.TaskEntity
import java.time.LocalDate

data class AddEditTaskUiState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val isCompleted: Boolean = false,
    val dueDate: LocalDate? = null,
    val isLoading: Boolean = false,
    val isSaveEnabled: Boolean = false,
    val errorMessage: String? = null
)