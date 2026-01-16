package com.example.checkit

import android.app.Application
import com.example.checkit.data.AppDataBase
import com.example.checkit.data.TaskRepository

class CheckItAplication: Application() {
    private val database by lazy { AppDataBase.getDatabase(this) }

    val repository by lazy { TaskRepository(database.taskDao()) }
}