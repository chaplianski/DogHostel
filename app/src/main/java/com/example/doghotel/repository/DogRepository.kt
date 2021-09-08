package com.example.doghotel.repository

import androidx.lifecycle.LiveData
import com.example.doghotel.model.Dog
import com.example.doghotel.db.room.DogDao

class DogRepository(private val dogDao: DogDao) {
 //   val readAllData: LiveData<List<Dog>> = dogDao.readAllData()

    suspend fun insertDog (dog: Dog){
        dogDao.insertDog(dog)
    }
}