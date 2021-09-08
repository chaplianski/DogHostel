package com.example.doghotel.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "dog_table")
data class Dog(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "dogID") var dogID: Int = 0,
    @ColumnInfo (name = "nickname") var nickname: String = "",
    @ColumnInfo (name = "gender") var gender: String = "",
    @ColumnInfo (name = "age") var age: Int = 1
 //   @ColumnInfo (name = "days") var days: Long = 1L

)
