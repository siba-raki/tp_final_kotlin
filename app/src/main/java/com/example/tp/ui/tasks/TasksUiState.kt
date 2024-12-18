
package com.example.tp.ui.tasks

import com.example.tp.data.local.entity.TaskEntity

// Representa el estado de la pantalla de tareas
data class TasksUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)