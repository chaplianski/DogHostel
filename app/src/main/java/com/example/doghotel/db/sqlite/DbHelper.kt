package com.example.doghotel.db.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.doghotel.model.Dog

class DbHelper (context: Context): SQLiteOpenHelper (
    context, DATA_BASE_NAME, null, DATA_BASE_VERSION
) {

    companion object{
        const val DATA_BASE_NAME = "dog_db"
        const val DATA_BASE_VERSION = 1

        const val TABLE_NAME = "dog_table"
        const val COLUMN_NAME_ID = "dogID"
        const val COLUMN_NAME_NICKNAME = "nickname"
        const val COLUMN_NAME_GENDER = "gender"
        const val COLUMN_NAME_AGE = "age"
    //    const val COLUMN_NAME_DAYS = "days"

        const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($COLUMN_NAME_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME_NICKNAME TEXT NOT NULL, $COLUMN_NAME_GENDER TEXT NOT NULL, $COLUMN_NAME_AGE INTEGER NOT NULL);"
    //            "$COLUMN_NAME_DAYS LONG NOT NULL);"
        const val SQL_DELITE_TABLE = "DROP TABLE IF EXIST $TABLE_NAME"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun getDogs (dogContext: Context): ArrayList<Dog> {
        val dogSelect = "Select * From $TABLE_NAME"
        val db = this.readableDatabase


        val cursor = db?.rawQuery(dogSelect, null)
        val dogs = ArrayList<Dog>()

        //  val cursor = db?.query(MyDbNamesClass.TABLE_NAME, null, null, null, null, null, null)

        if(cursor?.count == 0) {
  //          Toast.makeText(dogContext, "Database is empty", Toast.LENGTH_SHORT).show()
        } else {

            while (cursor?.moveToNext()!!) {
                val dog = Dog()
                dog.dogID = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID))
                dog.nickname = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICKNAME))
                dog.gender = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GENDER))
                dog.age = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_AGE))
            //    dog.days = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DAYS))
                dogs.add(dog)

                //    val  dataText = cursor.getString(cursor.getColumnIndex(MyDbNamesClass.COLUMN_NAME_TITLE))
            }
        }
        cursor.close()
        db.close()
        return dogs
    }

    fun addDog (dogContext: Context, dog: Dog){
        val values = ContentValues()
        values.put(COLUMN_NAME_NICKNAME, dog.nickname)
        values.put(COLUMN_NAME_GENDER, dog.gender)
        values.put(COLUMN_NAME_AGE, dog.age)
     //   values.put(COLUMN_NAME_DAYS, dog.days)
        val db = this.writableDatabase
        try {
            db.insert(TABLE_NAME, null, values)
        } catch (e: Exception){
            Toast.makeText(dogContext, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    fun deleteDog (dogID: Int): Boolean{
        val delete = "Delete From $TABLE_NAME where $COLUMN_NAME_ID = $dogID"
        val db = this.writableDatabase
        var deleteResult = false
        try {
         //   val cursor: Int = db.delete(TABLE_NAME, COLUMN_NAME_ID, arrayOf(dogID.toString()))
        val cursor: Unit = db.execSQL(delete)
            deleteResult = true
        } catch (e: Exception){
            Log.e(ContentValues.TAG, "Error Deleting")
        }
        db.close()
        return deleteResult
    }

    fun updateDog (dogContext: Context, dog: Dog){
        val db: SQLiteDatabase = this.writableDatabase
        val  contentValues = ContentValues()
  //      var updateResult = false
        contentValues.put(COLUMN_NAME_ID, dog.dogID)
        contentValues.put(COLUMN_NAME_NICKNAME, dog.nickname)
        contentValues.put(COLUMN_NAME_GENDER, dog.gender)
        contentValues.put(COLUMN_NAME_AGE, dog.age)
        try {
            db.update(TABLE_NAME,contentValues, "$COLUMN_NAME_ID = ?", arrayOf(dog.dogID.toString()))
  //          updateResult = true

        } catch (e: Exception){
            Log.e(ContentValues.TAG, "Error Updating")
     //       updateResult = false
        }
     //   return updateResult
    }

}




