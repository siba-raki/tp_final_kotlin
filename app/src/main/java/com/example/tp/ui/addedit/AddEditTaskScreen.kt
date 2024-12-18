package com.example.tp.ui.addedit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: String?,
    viewModel: AddEditTaskViewModel = hiltViewModel(),
    onTaskSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.id == 0)
                            "Agregar Nueva Tarea"
                        else
                            "Editar Tarea"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Cancelar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.saveTask()
                    onTaskSaved()
                },
            ) {
                Icon(Icons.Default.Save, contentDescription = "Guardar Tarea")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TextField(
                value = uiState.title,
                onValueChange = { viewModel.updateTitle(it) },
                label = { Text("Título de la tarea") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.title.length !in 3..50
            )
            if (uiState.title.length !in 3..50) {
                Text(
                    text = "El título debe tener entre 3 y 50 caracteres",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = uiState.description,
                onValueChange = { viewModel.updateDescription(it) },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = uiState.category,
                onValueChange = { viewModel.updateCategory(it) },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            DatePickerDialog(
                selectedDate = uiState.dueDate,
                onDateSelected = { viewModel.updateDueDate(it) }
            )

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
