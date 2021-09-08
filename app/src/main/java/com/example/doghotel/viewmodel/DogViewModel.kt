package com.example.doghotel.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.doghotel.model.Dog
import com.example.doghotel.db.room.DogDatabase
import com.example.doghotel.repository.DogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DogViewModel (application: Application): AndroidViewModel(application) {
/*
    val readAllData: LiveData<List<Dog>>
    private val repository: DogRepository

    init {
        val dogDao = DogDatabase.getDatabase(application).dogDao()
        repository = DogRepository (dogDao)
        readAllData = repository.readAllData
    }
    fun insertDog (dog: Dog){
        viewModelScope.launch { Dispatchers.IO
            repository.insertDog(dog)
        }
    }*/
}