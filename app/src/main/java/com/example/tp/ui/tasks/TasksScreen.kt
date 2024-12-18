package com.example.tp.ui.tasks


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tp.data.local.entity.TaskEntity
import com.example.tp.ui.theme.ThemeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
    onAddTask: () -> Unit,
    onEditTask: (TaskEntity) -> Unit,
    onTaskClick: (TaskEntity) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkMode by themeViewModel.isDarkMode.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") },
                actions = {
                    IconButton(onClick = { themeViewModel.toggleTheme() }) {
                        Icon(
                            imageVector = if (isDarkMode)
                                Icons.Default.LightMode
                            else
                                Icons.Default.DarkMode,
                            contentDescription = "Cambiar tema"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Tarea")
            }
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
                ErrorMessage(
                    message = uiState.error ?: "Error desconocido",
                    onRetry = { viewModel.loadTasks() }
                )
            }
            uiState.tasks.isEmpty() -> {
                EmptyTasksList()
            }
            else -> {
                TasksList(
                    modifier = Modifier.padding(padding),
                    tasks = uiState.tasks,
                    onTaskClick = onTaskClick,
                    onDeleteTask = { task -> viewModel.deleteTask(task) },
                    onEditTask = onEditTask,
                    onToggleComplete = { taskId, isCompleted ->
                        viewModel.toggleTaskCompletion(taskId, isCompleted)
                    }
                )
            }
        }
    }
}

@Composable
fun TasksList(
    modifier: Modifier = Modifier,
    tasks: List<TaskEntity>,
    onTaskClick: (TaskEntity) -> Unit,
    onDeleteTask: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit,
    onToggleComplete: (Int, Boolean) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onTaskClick = onTaskClick,
                onDeleteTask = onDeleteTask,
                onEditTask = onEditTask,
                onToggleComplete = onToggleComplete
            )
        }
    }
}

@Composable
fun TaskItem(
    task: TaskEntity,
    onTaskClick: (TaskEntity) -> Unit,
    onDeleteTask: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit,
    onToggleComplete: (Int, Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = {
                    onToggleComplete(task.id, it)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.isCompleted)
                        TextDecoration.LineThrough
                    else
                        null
                )
                task.description?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                task.dueDate?.let { dueDate ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Fecha de vencimiento",
                            modifier = Modifier.size(16.dp),
                            tint = if (dueDate < LocalDate.now())
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = dueDate.format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (dueDate < LocalDate.now())
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(
                onClick = { onEditTask(task) },
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar tarea",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = { onDeleteTask(task) },
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar tarea",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun EmptyTasksList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No hay tareas. ¡Agrega una nueva!")
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}