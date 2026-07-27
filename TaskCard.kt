package com.taskflow.app.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.taskflow.app.database.entity.Priority
import com.taskflow.app.database.entity.Task
import com.taskflow.app.theme.AccentBlue
import com.taskflow.app.theme.PriorityHigh
import com.taskflow.app.theme.PriorityLow
import com.taskflow.app.theme.PriorityMedium
import com.taskflow.app.theme.SubtleGray
import com.taskflow.app.theme.TextBlack
import com.taskflow.app.utils.DateUtils

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    isSelected: Boolean,
    selectionMode: Boolean,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
                    .background(PriorityHigh)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(onClick = onClick, onLongClick = onLongClick),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) Color(0xFFEFF6FF) else Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = task.completed,
                    onCheckedChange = { onToggleComplete() },
                    colors = CheckboxDefaults.colors(checkedColor = AccentBlue)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextBlack,
                        textDecoration = if (task.completed) TextDecoration.LineThrough else null,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (task.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SubtleGray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PriorityBadge(priority = task.priority)
                        if (task.category.isNotBlank()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            CategoryBadge(category = task.category)
                        }
                    }
                    if (task.dueDate != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        val overdue = DateUtils.isOverdue(task.dueDate, task.completed)
                        Text(
                            text = "Due ${DateUtils.formatDate(task.dueDate)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (overdue) PriorityHigh else SubtleGray
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = AccentBlue)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = PriorityHigh)
                    }
                }
            }
        }
    }
}

@Composable
private fun PriorityBadge(priority: Priority) {
    val (label, color) = when (priority) {
        Priority.LOW -> "Low" to PriorityLow
        Priority.MEDIUM -> "Medium" to PriorityMedium
        Priority.HIGH -> "High" to PriorityHigh
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = color, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun CategoryBadge(category: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AccentBlue.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = category, style = MaterialTheme.typography.bodySmall, color = AccentBlue, fontWeight = FontWeight.Medium)
    }
}