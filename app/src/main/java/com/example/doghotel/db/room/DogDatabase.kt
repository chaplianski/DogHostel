package com.example.doghotel.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.doghotel.model.Dog


@Database (entities = [Dog::class],version = 1,exportSchema = false)
    abstract class DogDatabase: RoomDatabase() {
        abstract fun dogDao(): DogDao

    companion object{
        @Volatile
        private var INSTANCE: DogDatabase? = null

        fun getDatabase (context: Context): DogDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DogDatabase::class.java,
                    "dog_db"
                ).build()
                INSTANCE = instance
                return instance

            }
        }
    }
}