package com.alexrdev.checkit.data

import com.alexrdev.checkit.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getTasks(): Flow<List<Task>>{
        return taskDao.getAlltasks()
    }

    suspend fun updateTaskCompleted(id: Int, completed: Boolean){
        taskDao.updateCompletedTask(id, completed)
    }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

}