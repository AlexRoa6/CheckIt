package com.example.checkit.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.checkit.R
import com.example.checkit.model.Task
import com.example.checkit.ui.theme.CheckItTheme
import com.example.checkit.ui.theme.Gray200
import com.example.checkit.ui.theme.Gray500
import com.example.checkit.ui.theme.Primary
import com.example.checkit.ui.theme.Shapes
import com.example.checkit.viewModel.HomeViewModel
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = { TopAppBar() },
        floatingActionButton = { ButtonNewTask(navController) }
    ) { it ->
        LazyColumn(contentPadding = it) {
            item { Title() }

            items(uiState.tasks, key = { it.id }) { task ->
                TaskCard(
                    task = task,
                    onTaskCompleted = { isChecked ->
                        viewModel.updateTaskCompletion(task, isChecked)
                    }
                )
            }
        }
    }
}


@Composable
fun TaskCard(task: Task, onTaskCompleted:(Boolean) -> Unit, modifier: Modifier = Modifier) {
    val isChecked = task.completed
    val formattedDate = task.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val colorTask = if (isChecked) Gray500 else Gray200
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onTaskCompleted,
                colors = CheckboxDefaults.colors(
                    checkedColor = Primary,
                    checkmarkColor = Gray200,
                )
            )
            Column {
                Text(
                    text = task.title,
                    textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                    color = colorTask

                )
                Text(
                    text = formattedDate,
                    color = colorTask
                )
            }
        }
    }
}

@Composable
fun Title(modifier: Modifier = Modifier) {
    Row(
        modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.calendar_check),
            contentDescription = stringResource(R.string.icono_calendar_description)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.mis_tareas),
            style = MaterialTheme.typography.headlineLarge,

            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayMedium,
            )
        },
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun ButtonNewTask(navController: NavController, modifier: Modifier = Modifier) {
    FloatingActionButton(
        { navController.navigate("formNewTask") },
        modifier
            .padding(16.dp)
            .size(64.dp),
        containerColor = Primary,
        contentColor = Gray200,
        shape = Shapes.extraLarge

    ) {
        Icon(
            painter = painterResource(R.drawable.add_2),
            contentDescription = stringResource(R.string.add_task_icon_button_description),
            Modifier.size(32.dp)
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    CheckItTheme {
        val navController = rememberNavController()
        HomeScreen(navController)
    }
}

