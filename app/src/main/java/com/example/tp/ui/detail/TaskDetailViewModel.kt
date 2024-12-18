package com.example.tp.ui.detail


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    init {
        val taskId = savedStateHandle.get<Int>("taskId")

        taskId?.let { loadTaskDetails(it) }
    }

    private fun loadTaskDetails(taskId: Int) {
        viewModelScope.launch {
            try {
                taskRepository.getTaskById(taskId)
                    .collect { task ->
                        _uiState.update {
                            it.copy(
                                task = task,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar los detalles de la tarea: ${e.message}"
                    )
                }
            }
        }
    }

    fun toggleTaskCompletion() {
        val currentTask = _uiState.value.task ?: return

        viewModelScope.launch {
            try {
                taskRepository.toggleTaskCompletion(
                    currentTask.id,
                    !currentTask.isCompleted
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Error al actualizar el estado de la tarea")
                }
            }
        }
    }

    fun deleteTask() {
        val currentTask = _uiState.value.task ?: return

        viewModelScope.launch {
            try {
                taskRepository.deleteTask(currentTask)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Error al eliminar la tarea")
                }
            }
        }
    }
}