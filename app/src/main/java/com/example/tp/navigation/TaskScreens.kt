package com.example.tp.navigation

sealed class TaskScreens(val route: String) {
    data object TaskList : TaskScreens("task_list")

    data object TaskDetail : TaskScreens("task_detail/{taskId}") {
        fun createRoute(taskId: Int) = "task_detail/$taskId"
    }

    data object AddEditTask : TaskScreens("add_edit_task/{taskId}") {
        fun createRoute(taskId: Int) = "add_edit_task/$taskId"
        fun createNewTaskRoute() = "add_edit_task/new"
    }
}