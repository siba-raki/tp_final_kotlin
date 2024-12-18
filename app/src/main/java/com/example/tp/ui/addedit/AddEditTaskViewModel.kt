package com.example.tp.ui.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp.data.local.entity.TaskEntity
import com.example.tp.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditTaskUiState())
    val uiState: StateFlow<AddEditTaskUiState> = _uiState.asStateFlow()

    private var originalTaskId: Int? = null

    init {
        val taskId = savedStateHandle.get<String>("taskId")

        if (taskId != null && taskId != "new") {
            taskId.takeIf { it != "new" }?.toIntOrNull()?.let { id ->
                originalTaskId = id
                loadTask(id)
            }
        }
    }

    private fun loadTask(taskId: Int) {
        viewModelScope.launch {
            try {
                taskRepository.getTaskById(taskId)
                    .collect { task ->
                        task?.let {
                            _uiState.update {
                                it.copy(
                                    id = it.id,
                                    title = it.title,
                                    description = it.description ?: "",
                                    category = it.category,
                                    isCompleted = it.isCompleted,
                                    dueDate = it.dueDate
                                )
                            }
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error al cargar la tarea: ${e.message}")
                }
            }
        }
    }

    fun saveTask() {
        val currentState = _uiState.value

        if (!validateTask(currentState.title)) {
            _uiState.update {
                it.copy(errorMessage = "El título debe tener entre 3 y 50 caracteres")
            }
            return
        }

        viewModelScope.launch {
            try {
                val taskToSave = TaskEntity(
                    id = originalTaskId ?: 0,
                    title = currentState.title.trim(),
                    description = currentState.description.takeIf { it.isNotBlank() },
                    category = currentState.category,
                    isCompleted = currentState.isCompleted,
                    dueDate = currentState.dueDate
                )

                taskRepository.insertTask(taskToSave)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al guardar la tarea: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(
                title = title,
                isSaveEnabled = validateTask(title)
            )
        }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun updateCategory(category: String) {
        _uiState.update { it.copy(category = category) }
    }

    fun updateDueDate(dueDate: LocalDate?) {
        _uiState.update { it.copy(dueDate = dueDate) }
    }

    private fun validateTask(title: String): Boolean {
        return title.trim().isNotEmpty() && title.length in 3..50
    }
}