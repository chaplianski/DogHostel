package com.example.doghotel

import android.R
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextUtils.substring
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.doghotel.databinding.ActivityDogCardBinding
import com.example.doghotel.db.room.DogDatabase
import com.example.doghotel.model.Dog
import com.example.doghotel.viewmodel.DogViewModel
import kotlinx.coroutines.*
import java.util.ArrayList

class DogCardActivity : AppCompatActivity() {
    var spiner_dog_gender_choice = ""
    lateinit var binding: ActivityDogCardBinding
//    lateinit var addViewModel: DogViewModel
    var gender: String? = null
    val genderArray = arrayListOf("Male", "Female")
    var arraySpinnerPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = this.getSharedPreferences(
            getString(com.example.doghotel.R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = resources.getString(com.example.doghotel.R.string.base_default_key)
        val baseVariant = sharedPref.getString(getString(com.example.doghotel.R.string.preference_file_key), defaultValue).toString()
  //

        val spinner = binding.tvDogCardGender
        val nickname = intent.extras?.getString("nick")
        gender = intent.extras?.getString("gend")
        val base = intent.extras?.getString("dbVariant")

        if(nickname != null) {
            for (index in genderArray.indices){
                if (genderArray[index] == gender) {
                     arraySpinnerPosition = index
                }
            }

            val age = intent.extras!!.getString("ag")
            binding.tvDogCardNickname.text = Editable.Factory.getInstance().newEditable(nickname)
          //  binding.tvDogCardGender.selectedItem(gender) = Editable.Factory.getInstance().newEditable(gender)
 //           spiner_dog_gender_choice = gender.toString()
  //          spinner.setSelection(index)


            binding.tvDogCardAge.text = Editable.Factory.getInstance().newEditable(age)
        }

        // Add dogs to database
        if (arraySpinnerPosition < 0){
            binding.btDogCardAdd.text = getString(com.example.doghotel.R.string.add_dog)

     //       binding.btDogCardDelete.isEnabled = false

            binding.btDogCardAdd.setOnClickListener(){

                if(binding.tvDogCardNickname.text.isEmpty()){
                    binding.tvDogCardNickname.requestFocus()
                }
                else {
                    if (binding.tvDogCardAge.text.isEmpty()) {
                        binding.tvDogCardAge.requestFocus()
                    } else {
                        val dog = Dog()
                        val nick = binding.tvDogCardNickname.text.toString()

                        dog.nickname = nickToUpperFirst(nick)
                        dog.gender = spiner_dog_gender_choice
                        dog.age = binding.tvDogCardAge.text.toString().toInt()
                    //    dog.days = SystemClock.elapsedRealtime()

                        Log.d("MyLog", "Сработало dogAdd")

                        if (baseVariant.toString().equals("SQLite")) {

                            CoroutineScope(Dispatchers.IO).launch {

                                MainActivity.dbHelper.addDog(this@DogCardActivity, dog)
                            }
                        }
                        if (baseVariant.toString().equals("Room")) {

                            CoroutineScope(Dispatchers.IO).launch {
  //                              Log.d("MyLog", "В догкарде пришла база ${baseVariant.toString()}")
                                val mDogDatabase = DogDatabase.getDatabase(this@DogCardActivity)
                                mDogDatabase.dogDao().insertDog(dog)
                            }
                            }

               //         }

                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                        finish()
                    }

                    }
                }

        }else {
            binding.btDogCardAdd.text = getString(com.example.doghotel.R.string.update)
            binding.btDogCardDelete.isEnabled = true
            binding.btDogCardAdd.setOnClickListener(){
                val dog = Dog()
                val position =  intent.extras!!.getInt("position")
                dog.dogID = position
                dog.nickname = binding.tvDogCardNickname.text.toString()
            //    spinner.setSelection(arraySpinnerPosition, true)
                dog.gender = spinner.selectedItem.toString()
                dog.age = binding.tvDogCardAge.text.toString().toInt()
   //             Log.d("MyLog", baseVariant.toString())

                if (baseVariant == "SQLite") {
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d("MyLog", "В догкарде сработала база ${baseVariant}")
                        MainActivity.dbHelper.updateDog(this@DogCardActivity, dog)
                    }
                }
                if (baseVariant == "Room") {
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d("MyLog", "В догкарде сработала база ${baseVariant}")
                        val mDogDatabase = DogDatabase.getDatabase(this@DogCardActivity)
                        mDogDatabase.dogDao().updateDog(dog)
                    }
                }
                val i = Intent (this, MainActivity::class.java)
                startActivity(i)
                finish()
            }

        }

   //     if (arraySpinnerPosition < 0) binding.btDogCardDelete.isEnabled = true

        binding.btDogCardDelete.setOnClickListener(){

            val dog = Dog()
            val position =  intent.extras!!.getInt("position")

            if (baseVariant.equals("SQLite")) {
                CoroutineScope(Dispatchers.IO).launch {

                    MainActivity.dbHelper.deleteDog(position)
                }
            }
            if (baseVariant.equals("Room")) {
                CoroutineScope(Dispatchers.IO).launch {

                    val mDogDatabase = DogDatabase.getDatabase(this@DogCardActivity)
                    dog.dogID = position
                    mDogDatabase.dogDao().deleteDog(dog)
                }
            }

        //    MainActivity.dbHelper.deleteDog(position)
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }




        val arrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, genderArray)
        spinner.adapter = arrayAdapter
      if (arraySpinnerPosition > -1) {
          spinner.setSelection(arraySpinnerPosition, true)
          var spinnerPos = spinner.setSelection(arraySpinnerPosition, true)
      }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                 spiner_dog_gender_choice = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }

        }
    }
     fun nickToUpperFirst (string: String): String{
         return substring(string,0,1).uppercase()+substring(string, 1, string.length)

     }
}


