package com.example.checkit.viewModel

import androidx.lifecycle.ViewModel
import com.example.checkit.model.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class NewTaskUiState(
    val title: String = "",
    val description: String = "",
    val dueDate: LocalDate = LocalDate.now(),
    val priority: Priority = Priority.Media,
    val isDatePickerVisible: Boolean = false,

)

class NewTaskViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(NewTaskUiState())
    val uiState: StateFlow<NewTaskUiState> = _uiState.asStateFlow()

    val priorityOptions: List<String> = Priority.entries.map { it.name }
    fun onTitleChange(newTitle: String){
        _uiState.update { it.copy(title = newTitle,) }
    }

    fun onDescriptionChange(newDescription: String){
        _uiState.update { it.copy(description = newDescription,) }
    }

    fun onDueDateChange(newDueDate: LocalDate){
        _uiState.update { it.copy(dueDate = newDueDate,) }
    }

    fun onPriorityChange(newPriority: String){
        val priority = Priority.valueOf(newPriority)
        _uiState.update { it.copy(priority = priority) }
    }

    fun showDatePicker(isVisible: Boolean){
        _uiState.update { it.copy(isDatePickerVisible = isVisible,) }
    }

    fun onDateSelected(millis: Long) {
        val newDate = Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        _uiState.update {

            it.copy(dueDate = newDate, isDatePickerVisible = false)
        }
    }

}