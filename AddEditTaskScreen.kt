package com.taskflow.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskflow.app.database.entity.Priority
import com.taskflow.app.database.entity.Task
import com.taskflow.app.theme.AccentBlue
import com.taskflow.app.theme.PriorityHigh
import com.taskflow.app.theme.PriorityLow
import com.taskflow.app.theme.PriorityMedium
import com.taskflow.app.theme.TextBlack
import com.taskflow.app.utils.DateUtils
import com.taskflow.app.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel,
    taskId: Int?,
    onDone: () -> Unit
) {
    var existingTask by remember { mutableStateOf<Task?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    var reminderEnabled by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var titleError by remember { mutableStateOf(false) }

    LaunchedEffect(taskId) {
        if (taskId != null && taskId != -1) {
            val task = viewModel.getTaskById(taskId)
            if (task != null) {
                existingTask = task
                title = task.title
                description = task.description
                category = task.category
                priority = task.priority
                dueDate = task.dueDate
                reminderEnabled = task.reminderEnabled
            }
        }
    }

    val isEditing = existingTask != null

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Edit Task" else "Add Task",
                        color = TextBlack,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextBlack)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it; titleError = false },
                label = { Text("Task Title") },
                isError = titleError,
                supportingText = { if (titleError) Text("Title is required", color = PriorityHigh) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AccentBlue)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AccentBlue)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AccentBlue)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Priority", style = MaterialTheme.typography.titleMedium, color = TextBlack)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PriorityOption("Low", PriorityLow, priority == Priority.LOW, Modifier.weight(1f)) { priority = Priority.LOW }
                PriorityOption("Medium", PriorityMedium, priority == Priority.MEDIUM, Modifier.weight(1f)) { priority = Priority.MEDIUM }
                PriorityOption("High", PriorityHigh, priority == Priority.HIGH, Modifier.weight(1f)) { priority = Priority.HIGH }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Due Date", style = MaterialTheme.typography.titleMedium, color = TextBlack)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Filled.DateRange, contentDescription = null, tint = AccentBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = DateUtils.formatDate(dueDate), color = TextBlack)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Reminder", style = MaterialTheme.typography.titleMedium, color = TextBlack)
                Switch(
                    checked = reminderEnabled,
                    onCheckedChange = { reminderEnabled = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AccentBlue)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDone,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        if (title.isBlank()) {
                            titleError = true
                        } else {
                            val current = existingTask
                            if (current != null) {
                                viewModel.updateTask(current, title, description, category, priority, dueDate, reminderEnabled)
                            } else {
                                viewModel.addTask(title, description, category, priority, dueDate, reminderEnabled)
                            }
                            onDone()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBlue, contentColor = Color.White)
                ) {
                    Text("Save")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dueDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dueDate = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) { Text("OK", color = AccentBlue) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun PriorityOption(
    label: String,
    color: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) color.copy(alpha = 0.15f) else Color.White
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = backgroundColor, contentColor = color)
    ) {
        Text(label)
    }
}