package com.taskflow.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val TaskFlowColorScheme = lightColorScheme(
    primary = AccentBlue,
    onPrimary = PureWhite,
    background = PureWhite,
    onBackground = TextBlack,
    surface = CardWhite,
    onSurface = TextBlack,
    surfaceVariant = PureWhite,
    onSurfaceVariant = SubtleGray,
    outline = DividerGray,
    error = PriorityHigh
)

@Composable
fun TaskFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TaskFlowColorScheme,
        typography = TaskFlowTypography,
        shapes = TaskFlowShapes,
        content = content
    )
}