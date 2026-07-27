package com.taskflow.app

import android.app.Application
import com.taskflow.app.database.TaskDatabase
import com.taskflow.app.repository.TaskRepository

class TaskFlowApplication : Application() {
    val database: TaskDatabase by lazy { TaskDatabase.getDatabase(this) }
    val repository: TaskRepository by lazy { TaskRepository(database.taskDao()) }
}