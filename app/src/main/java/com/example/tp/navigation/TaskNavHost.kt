package com.example.tp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import com.example.tp.ui.tasks.TasksScreen
import com.example.tp.ui.addedit.AddEditTaskScreen

@Composable
fun TaskNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = TaskScreens.TaskList.route
    ) {
        // Pantalla de lista de tareas
        composable(route = TaskScreens.TaskList.route) {
            TasksScreen(
                onAddTask = {
                    navController.navigate(TaskScreens.AddEditTask.createNewTaskRoute())
                },
                onTaskClick = { task ->
                    navController.navigate(TaskScreens.TaskDetail.createRoute(task.id))
                },
                onEditTask = { task ->
                    navController.navigate("add_edit_task/${task.id}")
                }
            )
        }

        // Pantalla de detalle de tarea TODO
        composable(
            route = TaskScreens.TaskDetail.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            // TaskDetailScreen(taskId = taskId)
        }

        // Pantalla de agregar/editar tarea
        composable(
            route = TaskScreens.AddEditTask.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")

            AddEditTaskScreen(
                taskId = taskId,
                onTaskSaved = {
                    navController.navigateUp()
                },
                onCancel = {
                    navController.navigateUp()
                }
            )
        }
    }
}