package com.example.tp.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp.data.local.entity.TaskEntity
import com.example.tp.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TasksUiState())

    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            taskRepository.getAllTasks()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error al cargar tareas: ${e.message}"
                        )
                    }
                }
                .collect { tasks ->
                    _uiState.update {
                        it.copy(
                            tasks = tasks,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            try {
                taskRepository.deleteTask(task)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Error al eliminar tarea: ${e.message}")
                }
            }
        }
    }

    fun toggleTaskCompletion(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                taskRepository.toggleTaskCompletion(taskId, isCompleted)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Error al actualizar tarea: ${e.message}")
                }
            }
        }
    }
}