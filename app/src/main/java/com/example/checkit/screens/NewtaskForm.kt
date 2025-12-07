package com.example.checkit.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.checkit.R
import com.example.checkit.ui.theme.CheckItTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.checkit.ui.theme.BackgroundDark
import com.example.checkit.ui.theme.Gray200
import com.example.checkit.ui.theme.IconBgDark
import com.example.checkit.ui.theme.IconBgLight
import com.example.checkit.ui.theme.Primary
import com.example.checkit.ui.theme.Shapes
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Funcion que compone  el UI de la pantalla para crear una nueva tarea
 */
@Composable
fun NewTaskForm(navController: NavHostController) {
    Scaffold(
        topBar = { TopBar { navController.popBackStack() } }
    ) { it ->
        Box(Modifier.padding(it)) {
            Form()
            SaveButton({ navController.popBackStack() }, Modifier.align(Alignment.BottomCenter))
        }

    }
}


/**
 * Compone el formulario completo
 */
@Composable
fun Form(modifier: Modifier = Modifier) {

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(8.dp)
                .align(Alignment.TopCenter)
        ) {
            TaskTitle()
            Spacer(Modifier.height(24.dp))
            TaskDescription()
            Spacer(Modifier.height(24.dp))
            DueDate()
            Spacer(Modifier.height(24.dp))
            TaskPriority()
        }
    }

}

/**
 * Elemento del formulario que representa el titulo de la nueva tarea
 */
@Composable
fun TaskTitle() {
    var titleOfTask by remember { mutableStateOf("") }
    Column {
        Text(
            text = stringResource(R.string.titleLabel),
            Modifier.padding(bottom = 8.dp, start = 4.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = titleOfTask,
            onValueChange = { titleOfTask = it },
            singleLine = true,
            placeholder = {
                Text(
                    stringResource(R.string.title_place_holder),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = IconBgDark,
                focusedContainerColor = IconBgDark,
                unfocusedBorderColor = BackgroundDark,
                focusedBorderColor = BackgroundDark
            ),
            shape = Shapes.medium,
        )
    }
}

/**
 * Elemento del formulario que representa la descripcion de la nueva tarea
 */
@Composable
fun TaskDescription() {
    var description by remember { mutableStateOf("") }
    Column {
        Text(
            text = stringResource(R.string.description_label),
            Modifier.padding(bottom = 8.dp, start = 4.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            singleLine = true,
            placeholder = {
                Box(Modifier.fillMaxHeight()) {
                    Text(
                        stringResource(R.string.description_placeholder),
                        Modifier.align(Alignment.TopStart),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = IconBgDark,
                focusedContainerColor = IconBgDark,
                unfocusedBorderColor = BackgroundDark,
                focusedBorderColor = BackgroundDark
            ),
            shape = Shapes.medium
        )
    }
}


/**
 * Elemento del formulario que representa la fecha de vencimiento de la nueva tarea
 */
@Composable
fun DueDate() {
    val date = remember { mutableStateOf(LocalDate.now()) }
    val showDatePicker = remember { mutableStateOf(false) }

    Column {
        Text(
            text = stringResource(R.string.fecha_label),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        OutlinedButton(
            onClick = { showDatePicker.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = Shapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(containerColor = IconBgDark),
            border = BorderStroke(1.dp, color = IconBgDark),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.calendar_month),
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(date.value.toString())
            }
        }
    }
    if (showDatePicker.value) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton({
                    datePickerState.selectedDateMillis?.let { millis ->
                        date.value = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showDatePicker.value = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton({
                    showDatePicker.value = false
                }) { Text("Cancelar") }
            },
            shape = Shapes.medium
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

/**
 * Elemento del formulario que representa la prioridad de la nueva tarea
 */
@Composable
fun TaskPriority() {
    val priorityOptions = listOf("Baja", "Media", "Alta")
    var selectedPriority by remember { mutableStateOf(priorityOptions[1]) }

    Column {
        Text(
            text = stringResource(R.string.priority_label),
            Modifier.padding(bottom = 8.dp, start = 4.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        PriorityToggle(
            options = priorityOptions,
            selectedOption = selectedPriority,
            onOptionSelected = { newPriority ->
                selectedPriority = newPriority
            }
        )

    }
}

/**
 * Compone las diferentes prioridades de la tarea em una Row
 */
@Composable
fun PriorityToggle(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth(),
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onOptionSelected(option) }
                    .background(color = if (isSelected) IconBgLight else IconBgDark)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = Gray200,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * Barra de arriba que contiene el titulo de la pantalla y el titulo de la pantalla
 * y el boton de cancelar la creacion de una nueva tarea
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onCancel: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background),
        navigationIcon = {
            Text(
                text = stringResource(R.string.cancelar),
                modifier = Modifier
                    .clickable { onCancel() }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = Primary
            )
        },

        title = {
            Text(
                stringResource(R.string.nueva_tarea),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    )
}


/**
 * Boton de guardar la nueva tarea creada
 */
@Composable
fun SaveButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        { onClick() },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = Shapes.medium
    ) {
        Text(
            text = stringResource(R.string.saveButon),
            style = MaterialTheme.typography.titleMedium,
            color = Gray200

        )
    }

}

@Preview
@Composable
fun FormPreview() {
    CheckItTheme {
        val navController = rememberNavController()
        NewTaskForm(navController)
    }
}