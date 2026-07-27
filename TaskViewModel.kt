package com.taskflow.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskflow.app.database.entity.Priority
import com.taskflow.app.database.entity.Task
import com.taskflow.app.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FilterOption { ALL, PENDING, COMPLETED }
enum class SortOption { DATE, PRIORITY, ALPHABETICAL }

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterOption = MutableStateFlow(FilterOption.ALL)
    val filterOption: StateFlow<FilterOption> = _filterOption.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.DATE)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _lastDeletedTask = MutableStateFlow<Task?>(null)
    val lastDeletedTask: StateFlow<Task?> = _lastDeletedTask.asStateFlow()

    private val allTasks: StateFlow<List<Task>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<Task>> = combine(
        allTasks, _searchQuery, _filterOption, _sortOption
    ) { taskList, query, filter, sort ->
        var result = taskList

        if (query.isNotBlank()) {
            result = result.filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
            }
        }

        result = when (filter) {
            FilterOption.ALL -> result
            FilterOption.PENDING -> result.filter { !it.completed }
            FilterOption.COMPLETED -> result.filter { it.completed }
        }

        result = when (sort) {
            SortOption.DATE -> result.sortedWith(compareBy(nullsLast()) { it.dueDate })
            SortOption.PRIORITY -> result.sortedByDescending {
                when (it.priority) {
                    Priority.HIGH -> 3
                    Priority.MEDIUM -> 2
                    Priority.LOW -> 1
                }
            }
            SortOption.ALPHABETICAL -> result.sortedBy { it.title.lowercase() }
        }

        result
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalTasks: StateFlow<Int> = allTasks
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedTasks: StateFlow<Int> = allTasks
        .map { list -> list.count { it.completed } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendingTasks: StateFlow<Int> = allTasks
        .map { list -> list.count { !it.completed } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completionPercentage: StateFlow<Int> = allTasks
        .map { list -> if (list.isEmpty()) 0 else (list.count { it.completed } * 100) / list.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilter(filter: FilterOption) {
        _filterOption.value = filter
    }

    fun updateSort(sort: SortOption) {
        _sortOption.value = sort
    }

    fun addTask(
        title: String,
        description: String,
        category: String,
        priority: Priority,
        dueDate: Long?,
        reminderEnabled: Boolean
    ) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insertTask(
                Task(
                    title = title.trim(),
                    description = description.trim(),
                    category = category.trim(),
                    priority = priority,
                    dueDate = dueDate,
                    reminderEnabled = reminderEnabled
                )
            )
        }
    }

    fun updateTask(
        task: Task,
        title: String,
        description: String,
        category: String,
        priority: Priority,
        dueDate: Long?,
        reminderEnabled: Boolean
    ) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.updateTask(
                task.copy(
                    title = title.trim(),
                    description = description.trim(),
                    category = category.trim(),
                    priority = priority,
                    dueDate = dueDate,
                    reminderEnabled = reminderEnabled,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun toggleCompleted(task: Task) {
        viewModelScope.launch {
            repository.setCompleted(task.id, !task.completed)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            _lastDeletedTask.value = task
        }
    }

    fun undoDelete() {
        val task = _lastDeletedTask.value ?: return
        viewModelScope.launch {
            repository.insertTask(task.copy(id = 0))
            _lastDeletedTask.value = null
        }
    }

    fun clearUndoState() {
        _lastDeletedTask.value = null
    }

    fun deleteTasks(taskIds: Set<Int>) {
        viewModelScope.launch {
            allTasks.value.filter { it.id in taskIds }.forEach { repository.deleteTask(it) }
        }
    }

    fun markAllCompleted() {
        viewModelScope.launch {
            repository.markAllCompleted()
        }
    }

    fun deleteCompleted() {
        viewModelScope.launch {
            repository.deleteCompletedTasks()
        }
    }

    suspend fun getTaskById(taskId: Int): Task? = repository.getTaskById(taskId)
}