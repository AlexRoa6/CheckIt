package com.example.checkit.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.checkit.CheckItAplication
import com.example.checkit.model.Task
import com.example.checkit.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(val tasks: List<Task> = emptyList())
class HomeViewModel(private val repository: TaskRepository) : ViewModel(){
    val uiState: StateFlow<HomeUiState> = repository.getTasks()
        .map { HomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )


    fun updateTaskCompletion(task: Task, isCompleted: Boolean){
        viewModelScope.launch {
            repository.updateTaskCompleted(task.id, isCompleted)
        }
    }

    fun onCLickDeleteTask(task: Task){
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val aplication = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CheckItAplication)
                HomeViewModel(aplication.repository)
            }
        }
    }
}