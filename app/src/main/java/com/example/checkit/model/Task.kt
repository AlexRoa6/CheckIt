package com.example.checkit.model


import java.time.LocalDate
import java.util.Date

enum class Priority{
    Baja, Media, Alta
}
data class Task (
    val id: Int,
    val title: String,
    val description: String = "",
    val completed: Boolean = false,
    val date: LocalDate,
    val priority: Priority = Priority.Media
)