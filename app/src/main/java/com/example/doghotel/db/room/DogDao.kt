package com.example.doghotel.db.room


import androidx.room.*
import com.example.doghotel.model.Dog


@Dao
interface DogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDog(dog: Dog)

    @Query("SELECT * FROM dog_table ORDER BY dogID")
    fun getAll(): List<Dog>

    @Update
    fun updateDog (dog: Dog)

    @Delete
    fun deleteDog (dog: Dog)

 //   @Query("SELECT cage FROM dog_table")
 //   fun getCagesArray(): ArrayList<Int>


}