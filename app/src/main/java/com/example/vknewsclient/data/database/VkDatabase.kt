package com.example.vknewsclient.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserIdDbModel::class], version = 3, exportSchema = false)
abstract class VkDatabase : RoomDatabase() {

    companion object{
        private val DB_NAME = "VkNewsDatabase"
        private var db_instance: VkDatabase? = null
        private val LOCK = Any()

        fun getInstance(application: Application): VkDatabase {
            db_instance?.let {
                return it
            }
            synchronized(LOCK) {
                db_instance?.let {  //Двойная проверка, вдруг второй поток будет ждать, пока первый создаст экземпляр БД и затем тоже его создаст
                    return it
                }
                val db = Room.databaseBuilder(
                    application.applicationContext,
                    VkDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                db_instance = db
                return db
            }
        }
    }

    abstract fun dao(): Dao
}