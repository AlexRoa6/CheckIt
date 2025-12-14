package com.example.checkit.model


object TaskRepository {
    private val tasks = mutableListOf<Task>()

    fun getTasks(): List<Task>{
        return tasks
    }

    fun updateTaskCompleted(id: Int, completed: Boolean){
        val index = tasks.indexOfFirst { it.id == id }

        if (index != -1) {
            val oldTask = tasks[index]
            val newTask = oldTask.copy(completed = completed)
            tasks[index] = newTask
        }
    }

    fun addTask(task: Task): Boolean {

        if(task.title.isNotBlank()){
            tasks.add(task)
            return true
        } else{
            return false
        }
    }

    fun deleteTask(task: Task) {
        tasks.remove(task)
    }
}
