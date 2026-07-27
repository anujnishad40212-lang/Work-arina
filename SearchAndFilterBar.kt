package com.taskflow.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.taskflow.app.theme.AccentBlue
import com.taskflow.app.viewmodel.FilterOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search tasks") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AccentBlue,
            unfocusedBorderColor = Color(0xFFE5E7EB),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChips(
    selected: FilterOption,
    onSelect: (FilterOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterOption.entries.forEach { option ->
            val label = when (option) {
                FilterOption.ALL -> "All"
                FilterOption.PENDING -> "Pending"
                FilterOption.COMPLETED -> "Completed"
            }
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(label) },
                shape = RoundedCornerShape(18.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AccentBlue,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}