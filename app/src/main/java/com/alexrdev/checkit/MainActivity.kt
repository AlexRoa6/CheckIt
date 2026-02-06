package com.alexrdev.checkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import com.alexrdev.checkit.view.HomeScreen
import com.alexrdev.checkit.view.NewTaskForm
import com.alexrdev.checkit.ui.theme.CheckItTheme
import com.alexrdev.checkit.viewModel.HomeViewModel
import com.alexrdev.checkit.viewModel.NewTaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckItTheme {
                AppNavigation()
            }
        }
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, 
        startDestination = "home",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable("home") {
            HomeScreen(navController, viewModel(factory = HomeViewModel.Factory))
        }
        composable("formNewTask") {
            NewTaskForm(navController,  viewModel(factory = NewTaskViewModel.Factory))
        }
    }
}

