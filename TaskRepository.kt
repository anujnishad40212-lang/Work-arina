package com.taskflow.app.repository

import com.taskflow.app.database.dao.TaskDao
import com.taskflow.app.database.entity.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun getTaskById(taskId: Int): Task? = taskDao.getTaskById(taskId)

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun deleteCompletedTasks() = taskDao.deleteCompletedTasks()

    suspend fun markAllCompleted(timestamp: Long = System.currentTimeMillis()) =
        taskDao.markAllCompleted(timestamp)

    suspend fun setCompleted(taskId: Int, completed: Boolean, timestamp: Long = System.currentTimeMillis()) =
        taskDao.setCompleted(taskId, completed, timestamp)
}