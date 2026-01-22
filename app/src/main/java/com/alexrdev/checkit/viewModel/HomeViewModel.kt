package com.alexrdev.checkit.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alexrdev.checkit.CheckItAplication
import com.alexrdev.checkit.model.Task
import com.alexrdev.checkit.data.TaskRepository
import com.alexrdev.checkit.model.TaskOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(val tasks: List<Task> = emptyList())
class HomeViewModel(private val repository: TaskRepository) : ViewModel(){
    private val _order = MutableStateFlow(TaskOrder.DATE)

    val uiState: StateFlow<HomeUiState> =
        combine(
            repository.getTasks(),
            _order
        ) { tasks, order ->
            val sortedTasks = when (order) {
                TaskOrder.PRIORITY ->
                    tasks.sortedByDescending { it.priority }

                TaskOrder.DATE ->
                    tasks.sortedBy { it.date }
            }
            HomeUiState(sortedTasks)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(emptyList())
        )

    fun changeOrder(order: TaskOrder) {
        _order.value = order
    }
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