package com.example.checkit.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

enum class Priority{
    Baja, Media, Alta
}
@Entity(tableName = "task_table")
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val completed: Boolean = false,
    val date: LocalDate,
    val priority: Priority = Priority.Media
)