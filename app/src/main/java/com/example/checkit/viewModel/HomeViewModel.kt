package com.example.checkit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkit.model.Task
import com.example.checkit.model.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(val tasks: List<Task> = emptyList())
class HomeViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }
    fun loadTasks(){
        val currentTasks = TaskRepository.getTasks().sortedBy { it.date }

        _uiState.update { currentState ->
            currentState.copy(tasks = currentTasks)
        }
    }

    fun updateTaskCompletion(task: Task, isCompleted: Boolean){
        viewModelScope.launch {
            TaskRepository.updateTaskCompleted(task.id, isCompleted)
            loadTasks()
        }
    }
}