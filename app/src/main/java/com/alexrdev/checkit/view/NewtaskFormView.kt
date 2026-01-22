package com.alexrdev.checkit.view

import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.alexrdev.checkit.R
import com.alexrdev.checkit.ui.theme.CheckItTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.alexrdev.checkit.model.Priority
import com.alexrdev.checkit.ui.theme.Gray200
import com.alexrdev.checkit.ui.theme.Shapes
import com.alexrdev.checkit.viewModel.NewTaskUiState
import com.alexrdev.checkit.viewModel.NewTaskViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Funcion que compone  el UI de la pantalla para crear una nueva tarea
 */
@Composable
fun NewTaskForm(navController: NavHostController, viewModel: NewTaskViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }

    // Cargar el anuncio intersticial
    DisposableEffect(Unit) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
        onDispose { interstitialAd = null }
    }

    Scaffold(
        topBar = { TopBar { navController.popBackStack() } }
    ) { it ->
        Box(Modifier.padding(it)) {
            Form(
                uiState = uiState,
                onTitleChange = viewModel::onTitleChange,
                onDescriptionChange = viewModel::onDescriptionChange,
                onPrioritySelected = viewModel::onPriorityChange,
                showDatePicker = viewModel::showDatePicker,
                onDateSelected = viewModel::onDateSelected
            )
            SaveButton(
                {
                    viewModel.onClickSaveButton {
                        val ad = interstitialAd
                        if (ad != null) {
                            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    navController.popBackStack()
                                }
                            }
                            ad.show(context as Activity)
                        } else {
                            navController.popBackStack()
                        }
                    }
                },
                Modifier.align(Alignment.BottomCenter)
            )
        }

    }
}


/**
 * Compone el formulario completo
 */
@Composable
fun Form(
    uiState: NewTaskUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPrioritySelected: (String) -> Unit,
    showDatePicker: (Boolean) -> Unit,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(8.dp)
                .align(Alignment.TopCenter)
        ) {
            TaskTitle(uiState.title, onTitleChange)
            Spacer(Modifier.height(24.dp))
            TaskDescription(uiState.description, onDescriptionChange)
            Spacer(Modifier.height(24.dp))
            DueDate(
                uiState.dueDate,
                showDatePicker = uiState.isDatePickerVisible,
                onDatePickerVisibilityChange = { showDatePicker(true) },
                onDateSelected = onDateSelected,
                onDismiss = { showDatePicker(false) }
            )
            Spacer(Modifier.height(24.dp))
            TaskPriority(uiState.priority, onPrioritySelected)
        }
    }
}

/**
 * Elemento del formulario que representa el titulo de la nueva tarea
 */
@Composable
fun TaskTitle(newTitle: String, onTitleChange: (String) -> Unit) {
    Column {
        Text(
            text = stringResource(R.string.titleLabel),
            Modifier.padding(bottom = 8.dp, start = 4.dp),
            style = MaterialTheme.typography.headlineSmall
        )
OutlinedTextField(
            value = newTitle,
            onValueChange = onTitleChange,
            singleLine = true,
            placeholder = {
                Text(
                    stringResource(R.string.title_place_holder),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier.fillMaxWidth()
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary), Shapes.medium)
                .shadow(
                elevation = 4.dp,
                shape = Shapes.medium
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = MaterialTheme.colorScheme.background,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            shape = Shapes.medium,
        )
    }
}

/**
 * Elemento del formulario que representa la descripcion de la nueva tarea
 */
@Composable
fun TaskDescription(newDescription: String, onDescriptionChange: (String) -> Unit) {

    Column {
        Text(
            text = stringResource(R.string.description_label),
            Modifier.padding(bottom = 8.dp, start = 4.dp),
            style = MaterialTheme.typography.headlineSmall
        )
OutlinedTextField(
            value = newDescription,
            onValueChange = onDescriptionChange,
            placeholder = {
                Text(
                    stringResource(R.string.description_placeholder),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary), Shapes.large)
                .shadow(
                    elevation = 4.dp,
                    shape = Shapes.large,
                ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.onSurface,
                unfocusedBorderColor = MaterialTheme.colorScheme.background,
                focusedBorderColor = MaterialTheme.colorScheme.background,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            shape = Shapes.large,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
        )
    }
}


/**
 * Elemento del formulario que representa la fecha de vencimiento de la nueva tarea
 */
@Composable
fun DueDate(
    date: LocalDate,
    showDatePicker: Boolean,
    onDateSelected: (Long) -> Unit,
    onDatePickerVisibilityChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant()
            .toEpochMilli()
    )

    Column(modifier.padding(horizontal = 4.dp)) {
        Text(
            text = stringResource(R.string.fecha_label),
            style = MaterialTheme.typography.headlineSmall
        )
        Row(
            modifier = Modifier
                .clickable { onDatePickerVisibilityChange(true) }
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.background),
                    Shapes.medium
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.calendar_month),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }

    if (showDatePicker) {
DatePickerDialog(
            onDismissRequest = { onDatePickerVisibilityChange(false) },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                }) {
                    Text(stringResource(R.string.saveDateButon), color = Gray200)
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text(stringResource(R.string.cancelar), color = Gray200)
                }
            },
            shape = Shapes.medium,
    colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.background),

        ) {
            DatePicker(
                state = datePickerState,
colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,

                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    headlineContentColor = MaterialTheme.colorScheme.onBackground,

                    weekdayContentColor = MaterialTheme.colorScheme.onBackground,
                    subheadContentColor = MaterialTheme.colorScheme.onBackground,

                    yearContentColor = MaterialTheme.colorScheme.onBackground,
                    currentYearContentColor = MaterialTheme.colorScheme.primary,
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = MaterialTheme.colorScheme.primary,

                    dayContentColor = MaterialTheme.colorScheme.onBackground,
                    disabledDayContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledSelectedDayContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledSelectedDayContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),

                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,

                    todayContentColor = MaterialTheme.colorScheme.primary,
                    todayDateBorderColor = MaterialTheme.colorScheme.primary,

                    dividerColor = MaterialTheme.colorScheme.outline,
                    navigationContentColor = MaterialTheme.colorScheme.onBackground,

                )
            )
        }
    }
}

/**
 * Elemento del formulario que representa la prioridad de la nueva tarea
 */
@Composable
fun TaskPriority(
    selectedPriority: Priority,
    onPrioritySelected: (String) -> Unit
) {
    val priorityOptions = Priority.entries.map { it.name }
    Column {
        Text(
            text = stringResource(R.string.priority_label),
            Modifier.padding(bottom = 8.dp, start = 4.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        PriorityToggle(
            options = priorityOptions,
            selectedOption = selectedPriority.name,
            onOptionSelected = onPrioritySelected
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
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = Shapes.medium
            ),
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onOptionSelected(option) }
                    .background(color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = MaterialTheme.colorScheme.onBackground,
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
            containerColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            Text(
                text = stringResource(R.string.cancelar),
                modifier = Modifier
                    .clickable { onCancel() }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        },

        title = {
            Text(
                stringResource(R.string.nueva_tarea),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
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
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground

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