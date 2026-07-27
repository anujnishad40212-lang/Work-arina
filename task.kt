package com.taskflow.app.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Priority {
    LOW, MEDIUM, HIGH
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val category: String = "",
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null,
    val reminderEnabled: Boolean = false,
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)