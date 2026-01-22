package com.alexrdev.checkit.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.alexrdev.checkit.R
import com.alexrdev.checkit.model.Priority
import com.alexrdev.checkit.model.Task
import com.alexrdev.checkit.model.TaskOrder
import com.alexrdev.checkit.ui.theme.CheckItTheme
import com.alexrdev.checkit.ui.theme.Shapes
import com.alexrdev.checkit.viewModel.HomeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = { TopAppBar() },
        floatingActionButton = { ButtonNewTask(navController) },
        bottomBar = { AdBanner() }
    ) { paddingValues ->
        LazyColumn(contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding() + 16.dp, start = 16.dp, end = 16.dp, top = paddingValues.calculateTopPadding())) {
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
fun AdBanner(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)

                adUnitId = "ca-app-pub-9196016042691783/1299391688"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}


@Composable
fun TaskCard(task: Task, onTaskCompleted:(Boolean) -> Unit, deleteTask: () -> Unit, modifier: Modifier = Modifier) {
    val isChecked = task.completed
    var showDescription by remember { mutableStateOf(false) }
    val formattedDate = task.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val colorTask = if (isChecked) MaterialTheme.colorScheme.onPrimaryFixed else MaterialTheme.colorScheme.onBackground
    val borderColor = when (task.priority) {
        Priority.Alta -> MaterialTheme.colorScheme.error
        Priority.Media -> MaterialTheme.colorScheme.scrim
        else -> MaterialTheme.colorScheme.onTertiary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .borderSoloIzquierdo(borderColor, 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!isChecked) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.secondary,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = Shapes.large,
        border = BorderStroke(  1.dp, if (!isChecked) MaterialTheme.colorScheme.tertiary
        else MaterialTheme.colorScheme.onSecondary,),
        onClick = {showDescription = !showDescription},
    ) {
            Row(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, top = 16.dp, end = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = onTaskCompleted,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = MaterialTheme.colorScheme.onBackground,
                        uncheckedColor = MaterialTheme.colorScheme.onBackground
                    )
                )
                Column (Modifier.weight(1f)
                    .animateContentSize(
                        animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )){
                    Text(
                        text = task.title,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                        color = colorTask,
                        style = MaterialTheme.typography.bodyLarge

                    )
                    Text(
                        text = formattedDate,
                        color = colorTask,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(5.dp))
                    if (showDescription && task.description.isNotBlank()) {
                        Text(
                            text = task.description,
                            color = colorTask,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                IconButton(
                    deleteTask,
                    modifier = Modifier
                        .padding(8.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    shape = Shapes.medium
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete_icon),
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
                contentDescription = stringResource(R.string.icono_calendar_description),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.mis_tareas),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
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
                color = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary
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
        contentColor = MaterialTheme.colorScheme.onBackground,
        shape = Shapes.extraLarge

    ) {
        Icon(
            painter = painterResource(R.drawable.add_2),
            contentDescription = stringResource(R.string.add_task_icon_button_description),
            Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}


fun Modifier.borderSoloIzquierdo(color: Color, width: Dp): Modifier = this.drawBehind {
    val strokeWidth = width.toPx()
    val cornerRadius = 24.dp.toPx()
    val verticalPadding = 16.dp.toPx()

    drawRoundRect(
        color = color,
        topLeft = Offset(0f, verticalPadding),
        size = Size(strokeWidth, size.height - (verticalPadding * 2)),
        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        style = Stroke(width = strokeWidth)
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
                contentDescription = stringResource(R.string.ordenar),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            DropdownMenuItem(
                text = { 
                    Text(
                        stringResource(R.string.por_prioridad),
                        color = MaterialTheme.colorScheme.onBackground
                    ) 
                },
                onClick = {
                    onOrderSelected(TaskOrder.PRIORITY)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { 
                    Text(
                        stringResource(R.string.por_fecha),
                        color = MaterialTheme.colorScheme.onBackground
                    ) 
                },
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

