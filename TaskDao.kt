package com.taskflow.app.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.taskflow.app.database.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: Int): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE completed = 1")
    suspend fun deleteCompletedTasks()

    @Query("UPDATE tasks SET completed = 1, updatedAt = :timestamp")
    suspend fun markAllCompleted(timestamp: Long)

    @Query("UPDATE tasks SET completed = :completed, updatedAt = :timestamp WHERE id = :taskId")
    suspend fun setCompleted(taskId: Int, completed: Boolean, timestamp: Long)
}