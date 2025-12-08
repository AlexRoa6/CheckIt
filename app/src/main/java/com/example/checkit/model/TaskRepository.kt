package com.example.checkit.model

import java.time.LocalDate

object TaskRepository {
    private val tasks = mutableListOf(
        Task(
            id = 1,
            title = "Hacer la cama",
            description = "Ordenar y hacer la cama por la mañana",
            completed = false,
            date = LocalDate.of(2025, 12, 5)
        ),
        Task(
            id = 2,
            title = "Comprar comida",
            description = "Ir al supermercado y comprar leche, pan y huevos",
            completed = false,
            date = LocalDate.of(2025, 12, 6)
        ),
        Task(
            id = 3,
            title = "Estudiar Kotlin",
            description = "Practicar Jetpack Compose y estructuras de datos",
            completed = true,
            date = LocalDate.of(2025, 12, 4)
        ),
        Task(
            id = 4,
            title = "Hacer deporte",
            description = "Correr 5 km por la tarde",
            completed = false,
            date = LocalDate.of(2025, 12, 5)
        ),
        Task(
            id = 5,
            title = "Llamar al médico",
            description = "Pedir cita para revisión anual",
            completed = false,
            date = LocalDate.of(2025, 12, 7)
        ),
        Task(
            id = 6,
            title = "Leer un libro",
            description = "Terminar capítulo pendiente del libro de programación",
            completed = true,
            date = LocalDate.of(2025, 12, 3)
        )
    )

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
}
