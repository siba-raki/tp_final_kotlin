package com.example.tp.ui.detail


import com.example.tp.data.local.entity.TaskEntity
import java.time.LocalDate

data class TaskDetailUiState(
    val task: TaskEntity? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)