package com.example.tp.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel = hiltViewModel(),
    onEditTask: (Int) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Tarea") },
            text = { Text("¿Estás seguro de que quieres eliminar esta tarea?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTask()
                        onBack()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles de la Tarea") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            uiState.task?.let { task ->
                                onEditTask(task.id)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            uiState.task != null -> {
                val task = uiState.task!!

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Estado: ",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Switch(
                            checked = task.isCompleted,
                            onCheckedChange = { viewModel.toggleTaskCompletion() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )

                        Text(
                            text = if (task.isCompleted) "Completada" else "Pendiente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (task.isCompleted) Color.Green else Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    task.description?.let { description ->
                        Text(
                            text = "Descripción:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Text(
                        text = "Categoría: ${task.category}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Creada el: ${task.createdAt}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    task.dueDate?.let { dueDate ->
                        Text(
                            text = "Fecha de vencimiento: $dueDate",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}