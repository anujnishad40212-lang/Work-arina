package com.taskflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.taskflow.app.navigation.TaskFlowNavGraph
import com.taskflow.app.theme.TaskFlowTheme
import com.taskflow.app.viewmodel.TaskViewModel
import com.taskflow.app.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as TaskFlowApplication
        val factory = TaskViewModelFactory(app.repository)

        setContent {
            TaskFlowTheme {
                val viewModel: TaskViewModel = viewModel(factory = factory)
                TaskFlowNavGraph(viewModel = viewModel)
            }
        }
    }
}