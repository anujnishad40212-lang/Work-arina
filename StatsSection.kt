package com.taskflow.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskflow.app.theme.AccentBlue
import com.taskflow.app.theme.SubtleGray
import com.taskflow.app.theme.TextBlack

@Composable
fun StatsSection(
    total: Int,
    completed: Int,
    pending: Int,
    completionPercentage: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(label = "Total", value = total.toString())
                StatItem(label = "Completed", value = completed.toString())
                StatItem(label = "Pending", value = pending.toString())
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { completionPercentage / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = AccentBlue,
                    trackColor = Color(0xFFE5E7EB)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$completionPercentage%",
                    fontWeight = FontWeight.SemiBold,
                    color = TextBlack
                )
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(text = value, style = MaterialTheme.typography.headlineMedium, color = TextBlack)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
    }
}