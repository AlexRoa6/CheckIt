package com.example.checkit

import android.app.Application
import com.example.checkit.data.AppDataBase
import com.example.checkit.data.TaskRepository
import com.google.android.gms.ads.MobileAds

class CheckItAplication: Application() {
    private val database by lazy { AppDataBase.getDatabase(this) }

    val repository by lazy { TaskRepository(database.taskDao()) }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}