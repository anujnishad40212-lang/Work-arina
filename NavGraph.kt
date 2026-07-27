package com.taskflow.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.taskflow.app.screens.AddEditTaskScreen
import com.taskflow.app.screens.DashboardScreen
import com.taskflow.app.viewmodel.TaskViewModel

object Routes {
    const val DASHBOARD = "dashboard"
    const val ADD_EDIT_TASK = "add_edit_task"
    const val TASK_ID_ARG = "taskId"
}

@Composable
fun TaskFlowNavGraph(viewModel: TaskViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.DASHBOARD) {
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                viewModel = viewModel,
                onAddTask = { navController.navigate("${Routes.ADD_EDIT_TASK}/-1") },
                onEditTask = { id -> navController.navigate("${Routes.ADD_EDIT_TASK}/$id") }
            )
        }
        composable(
            route = "${Routes.ADD_EDIT_TASK}/{${Routes.TASK_ID_ARG}}",
            arguments = listOf(navArgument(Routes.TASK_ID_ARG) { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt(Routes.TASK_ID_ARG) ?: -1
            AddEditTaskScreen(
                viewModel = viewModel,
                taskId = taskId,
                onDone = { navController.popBackStack() }
            )
        }
    }
}