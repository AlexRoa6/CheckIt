package com.example.checkit.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.checkit.R
import com.example.checkit.model.Task
import com.example.checkit.model.TaskRepository.tasks
import com.example.checkit.ui.theme.CheckItTheme
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(){
    Scaffold (
        topBar = { TopAppBar() }
    ){ it ->
        LazyColumn (contentPadding = it){
            items(tasks) {
                TaskCard(
                    it
                )
            }
        }

    }
}


@Composable
fun TaskCard(task: Task, modifier: Modifier = Modifier){
    var isChecked by remember { mutableStateOf(task.completed) }
    val formattedDate = task.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row (modifier = Modifier.padding(16.dp)){
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = !isChecked }
            )
            Column {
                Text(
                    text = task.title
                )
                Text(
                    text = formattedDate
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(modifier: Modifier = Modifier){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewHomeScreen(){
    CheckItTheme {
        HomeScreen()
    }
}

