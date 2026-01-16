package com.example.checkit.view

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.checkit.R
import com.example.checkit.model.Priority
import com.example.checkit.model.Task
import com.example.checkit.model.TaskOrder
import com.example.checkit.ui.theme.CheckItTheme
import com.example.checkit.ui.theme.FooterDeleteBgDark
import com.example.checkit.ui.theme.Gray200
import com.example.checkit.ui.theme.Gray500
import com.example.checkit.ui.theme.Primary
import com.example.checkit.ui.theme.PriorityHigh
import com.example.checkit.ui.theme.PriorityLow
import com.example.checkit.ui.theme.PriorityMedium
import com.example.checkit.ui.theme.Shapes
import com.example.checkit.viewModel.HomeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = { TopAppBar() },
        floatingActionButton = { ButtonNewTask(navController) }
    ) { it ->
        LazyColumn(contentPadding = it) {
            item { Title(onOrderSelected = { order -> viewModel.changeOrder(order)}) }

            items(uiState.tasks, key = { it.id }) { task ->
                TaskCard(
                    task = task,
                    onTaskCompleted = { isChecked ->
                        viewModel.updateTaskCompletion(task, isChecked)
                    },
                    deleteTask = { viewModel.onCLickDeleteTask(task)}
                )
            }
        }
    }
}


@Composable
fun TaskCard(task: Task, onTaskCompleted:(Boolean) -> Unit, deleteTask: () -> Unit, modifier: Modifier = Modifier) {
    val isChecked = task.completed
    val formattedDate = task.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val colorTask = if (isChecked) Gray500 else Gray200
    val borderColor = when (task.priority) {
        Priority.Alta -> MaterialTheme.colorScheme.error
        Priority.Media -> MaterialTheme.colorScheme.scrim
        else -> MaterialTheme.colorScheme.onTertiary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .borderSoloIzquierdo(borderColor, 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Box(Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp)) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = onTaskCompleted,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = MaterialTheme.colorScheme.surface,
                        uncheckedColor = MaterialTheme.colorScheme.tertiary
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
            IconButton(deleteTask,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(8.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shape = Shapes.medium
            ) {
                Icon(painter = painterResource(R.drawable.delete_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        }
    }
}

@Composable
fun Title(modifier: Modifier = Modifier, onOrderSelected: (TaskOrder) -> Unit) {
    Box(modifier
        .fillMaxWidth()
        .padding(8.dp)){
        Row (modifier.align(Alignment.CenterStart)){
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
        Box(modifier.align(Alignment.CenterEnd)) {
            OrderDropdownMenu(
                onOrderSelected = onOrderSelected
            )
        }
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
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.surface,
        shape = Shapes.extraLarge

    ) {
        Icon(
            painter = painterResource(R.drawable.add_2),
            contentDescription = stringResource(R.string.add_task_icon_button_description),
            Modifier.size(32.dp)
        )
    }
}


fun Modifier.borderSoloIzquierdo(color: Color, width: Dp): Modifier = this.drawBehind {

    val strokeWidth = width.toPx()
    val y = size.height - strokeWidth / 2


    drawLine(
        color = color,
        start = Offset(0f, 0f),
        end = Offset(0f, size.height),
        strokeWidth = strokeWidth
    )

}

@Composable
fun OrderDropdownMenu(
    onOrderSelected: (TaskOrder) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(R.drawable.baseline_filter_list_24),
                contentDescription = stringResource(R.string.ordenar)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.por_prioridad)) },
                onClick = {
                    onOrderSelected(TaskOrder.PRIORITY)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text(stringResource(R.string.por_fecha)) },
                onClick = {
                    onOrderSelected(TaskOrder.DATE)
                    expanded = false
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    CheckItTheme {
        val task: Task = Task(
            id = 1,
            date = LocalDate.now(),
            title = "pepe",
            description = "sdkjflsdkfj",
            priority = Priority.Alta,
            completed = false
        )
        TaskCard(task, {}, {})

    }}

