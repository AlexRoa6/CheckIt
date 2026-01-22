package com.alexrdev.checkit

import android.app.Application
import com.alexrdev.checkit.data.AppDataBase
import com.alexrdev.checkit.data.TaskRepository
import com.google.android.gms.ads.MobileAds

class CheckItAplication: Application() {
    private val database by lazy { AppDataBase.getDatabase(this) }

    val repository by lazy { TaskRepository(database.taskDao()) }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}