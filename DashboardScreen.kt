package com.taskflow.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskflow.app.components.EmptyState
import com.taskflow.app.components.FilterChips
import com.taskflow.app.components.SearchBar
import com.taskflow.app.components.StatsSection
import com.taskflow.app.components.TaskCard
import com.taskflow.app.theme.AccentBlue
import com.taskflow.app.theme.PriorityHigh
import com.taskflow.app.theme.SubtleGray
import com.taskflow.app.theme.TextBlack
import com.taskflow.app.utils.DateUtils
import com.taskflow.app.viewmodel.SortOption
import com.taskflow.app.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: TaskViewModel,
    onAddTask: () -> Unit,
    onEditTask: (Int) -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val filter by viewModel.filterOption.collectAsState()
    val total by viewModel.totalTasks.collectAsState()
    val completed by viewModel.completedTasks.collectAsState()
    val pending by viewModel.pendingTasks.collectAsState()
    val percentage by viewModel.completionPercentage.collectAsState()
    val lastDeleted by viewModel.lastDeletedTask.collectAsState()

    var selectionMode by remember { mutableStateOf(false) }
    val selectedIds: SnapshotStateList<Int> = remember { mutableStateListOf() }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(lastDeleted) {
        val deleted = lastDeleted ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = "\"${deleted.title}\" deleted",
            actionLabel = "Undo",
            duration = SnackbarDuration.Short
        )
        if (result == SnackbarResult.ActionPerformed) {
            viewModel.undoDelete()
        } else {
            viewModel.clearUndoState()
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (!selectionMode) {
                FloatingActionButton(
                    onClick = onAddTask,
                    containerColor = AccentBlue,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Task")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            if (selectionMode) {
                SelectionToolbar(
                    selectedCount = selectedIds.size,
                    onSelectAll = {
                        selectedIds.clear()
                        selectedIds.addAll(tasks.map { it.id })
                    },
                    onDeleteSelected = {
                        viewModel.deleteTasks(selectedIds.toSet())
                        selectedIds.clear()
                        selectionMode = false
                    },
                    onCancel = {
                        selectedIds.clear()
                        selectionMode = false
                    }
                )
            } else {
                Text(
                    text = DateUtils.greeting(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextBlack,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = DateUtils.formatTodayLong(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubtleGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            StatsSection(
                total = total,
                completed = completed,
                pending = pending,
                completionPercentage = percentage
            )

            Spacer(modifier = Modifier.height(16.dp))

            SearchBar(query = query, onQueryChange = viewModel::updateSearchQuery)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChips(
                    selected = filter,
                    onSelect = viewModel::updateFilter,
                    modifier = Modifier.weight(1f)
                )
                Box {
                    IconButton(onClick = { sortMenuExpanded = true }) {
                        Icon(Icons.Filled.DoneAll, contentDescription = "Sort and actions", tint = SubtleGray)
                    }
                    DropdownMenu(expanded = sortMenuExpanded, onDismissRequest = { sortMenuExpanded = false }) {
                        DropdownMenuItem(text = { Text("Sort by Date") }, onClick = {
                            viewModel.updateSort(SortOption.DATE); sortMenuExpanded = false
                        })
                        DropdownMenuItem(text = { Text("Sort by Priority") }, onClick = {
                            viewModel.updateSort(SortOption.PRIORITY); sortMenuExpanded = false
                        })
                        DropdownMenuItem(text = { Text("Sort Alphabetically") }, onClick = {
                            viewModel.updateSort(SortOption.ALPHABETICAL); sortMenuExpanded = false
                        })
                        HorizontalDivider()
                        DropdownMenuItem(text = { Text("Mark All Completed") }, onClick = {
                            viewModel.markAllCompleted(); sortMenuExpanded = false
                        })
                        DropdownMenuItem(text = { Text("Delete Completed") }, onClick = {
                            viewModel.deleteCompleted(); sortMenuExpanded = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (tasks.isEmpty()) {
                EmptyState(
                    message = if (query.isNotBlank()) "No tasks match your search" else "No tasks yet. Tap + to add one."
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(tasks, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            isSelected = selectedIds.contains(task.id),
                            selectionMode = selectionMode,
                            onToggleComplete = { viewModel.toggleCompleted(task) },
                            onEdit = { onEditTask(task.id) },
                            onDelete = { viewModel.deleteTask(task) },
                            onClick = {
                                if (selectionMode) {
                                    toggleSelection(selectedIds, task.id)
                                    if (selectedIds.isEmpty()) selectionMode = false
                                } else {
                                    onEditTask(task.id)
                                }
                            },
                            onLongClick = {
                                selectionMode = true
                                toggleSelection(selectedIds, task.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun toggleSelection(list: SnapshotStateList<Int>, id: Int) {
    if (list.contains(id)) list.remove(id) else list.add(id)
}

@Composable
private fun SelectionToolbar(
    selectedCount: Int,
    onSelectAll: () -> Unit,
    onDeleteSelected: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCancel) {
            Icon(Icons.Filled.Close, contentDescription = "Cancel selection")
        }
        Text(
            text = "$selectedCount selected",
            style = MaterialTheme.typography.titleMedium,
            color = TextBlack
        )
        Row {
            TextButton(onClick = onSelectAll) {
                Text("Select All", color = AccentBlue)
            }
            IconButton(onClick = onDeleteSelected) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete selected", tint = PriorityHigh)
            }
        }
    }
}