package com.example.doghotel.db.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.doghotel.model.Dog


@Dao
interface DogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDog(dog: Dog)

    @Query("SELECT * FROM dog_table ORDER BY dogID")
    fun getAll(): List<Dog>
//    @Query("SELECT * FROM dog_table ORDER BY dogID ASC")
//    fun readAllData(): LiveData<List<Dog>>

    @Update
    fun updateDog (dog: Dog)

    @Delete
    fun deleteDog (dog: Dog)

}