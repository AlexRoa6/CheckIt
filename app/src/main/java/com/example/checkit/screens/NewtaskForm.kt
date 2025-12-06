package com.example.checkit.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkit.ui.theme.BackgroundDark
import com.example.checkit.ui.theme.Gray200
import com.example.checkit.ui.theme.Gray600
import com.example.checkit.ui.theme.Gray700
import com.example.checkit.ui.theme.IconBgDark
import com.example.checkit.ui.theme.IconBgLight
import com.example.checkit.ui.theme.Primary
import com.example.checkit.ui.theme.Shapes
import com.example.checkit.ui.theme.TextSecondaryLight
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NewTaskForm(navController: NavHostController) {
    Scaffold (
        topBar = { TopBar({ navController.popBackStack() }) }
    ){ it ->
        Box(Modifier.padding(it)){
            FormNewTask()
        }

    }
}

@Composable
fun FormNewTask(modifier: Modifier = Modifier){

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.padding(8.dp).align(Alignment.TopCenter)) {
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

@Composable
fun TaskTitle(){
    var titleOfTask by remember { mutableStateOf("") }
    Column {
        Text(
            text = stringResource(R.string.titleLabel),
            Modifier.padding(bottom = 8.dp),
            fontSize = 20.sp
        )
        OutlinedTextField(
            value = titleOfTask,
            onValueChange = { titleOfTask = it },
            singleLine = true,
            placeholder = { Text(stringResource(R.string.title_place_holder)) },
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

@Composable
fun TaskDescription(){
    var description by remember { mutableStateOf("") }
    Column {
        Text(
            text = stringResource(R.string.description_label),
            Modifier.padding(bottom = 8.dp),
            fontSize = 20.sp
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            singleLine = true,
            placeholder = {
                Box(Modifier.fillMaxHeight()) {
                    Text(stringResource(R.string.description_placeholder),
                        Modifier.align(Alignment.TopStart))
                }
            },
            modifier = Modifier.fillMaxWidth().height(128.dp),
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

@Composable
fun DueDate(){
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }

    Column {
        Text(text = stringResource(R.string.dueDate_label),
            Modifier.padding(bottom = 8.dp),
            fontSize = 20.sp
        )

        TextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Icon(
                    painter = painterResource(R.drawable.calendar_month),
                    contentDescription = null,
                )

                Text(stringResource(R.string.dueDate_plcaeholder),
                    modifier = Modifier.padding(start = 32.dp))
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = IconBgDark,
                focusedContainerColor = IconBgDark,
                unfocusedBorderColor = BackgroundDark,
                focusedBorderColor = BackgroundDark
            ),
            shape = Shapes.medium
        )

        if (showDialog) {
            DatePickerModal(
                onDateSelected = { dateMillis ->
                    dateMillis?.let {
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        selectedDate = sdf.format(it)
                    }
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun TaskPriority(){
    val priorityOptions = listOf("Baja", "Media", "Alta")
    var selectedPriority by remember { mutableStateOf(priorityOptions[1]) }

    Column {
        Text(
            text = "Prioridad",
            Modifier.padding(bottom = 8.dp),
            fontSize = 20.sp
        )
        PriorityToggle(
            options = priorityOptions,
            selectedOption = selectedPriority,
            onOptionSelected = { newPriority ->
                // Actualiza el estado cuando se selecciona una nueva prioridad
                selectedPriority = newPriority
            }
        )

    }
}

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
                    color = Gray200
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onCancel: () -> Unit){
    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
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
            Text(stringResource(R.string.nueva_tarea),
                fontSize = 24.sp
            )
        }
    )
}
@Preview
@Composable
fun FormPreview(){
    CheckItTheme {
        FormNewTask()
    }
}