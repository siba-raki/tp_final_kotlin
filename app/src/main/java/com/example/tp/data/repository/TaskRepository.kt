package com.example.tp.data.repository


import com.example.tp.data.local.dao.TaskDao
import com.example.tp.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()

    fun getActiveTasks(): Flow<List<TaskEntity>> = taskDao.getActiveTasks()

    fun getCompletedTasks(): Flow<List<TaskEntity>> = taskDao.getCompletedTasks()

    fun getTaskById(taskId: Int): Flow<TaskEntity?> = taskDao.getTaskById(taskId)

    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)

    suspend fun insertTask(task: TaskEntity) = taskDao.insertTask(task)

    suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)

    suspend fun toggleTaskCompletion(taskId: Int, completed: Boolean) =
        taskDao.toggleTaskCompletion(taskId, completed)
}