package com.example.checkit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.checkit.model.Converters
import com.example.checkit.model.Task

@Database(entities = [Task::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDataBase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object{
        @Volatile
        private var Instance: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase{
            // Si Instance no es nula devuleve la Instance existente
            // Si es null crea la database
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, AppDataBase::class.java, "checkIt_db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}