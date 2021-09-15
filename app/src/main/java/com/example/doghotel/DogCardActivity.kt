package com.example.doghotel

import android.R
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils.substring
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.doghotel.databinding.ActivityDogCardBinding
import com.example.doghotel.db.room.DogDatabase
import com.example.doghotel.model.Dog
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class DogCardActivity : AppCompatActivity() {

    var spinnerDogGenderChoice = ""
    var spinnerDogCageChoice = ""
    private lateinit var binding: ActivityDogCardBinding
    var gender: String? = null
    private val genderArray = arrayListOf("Male", "Female")
    private var arraySpinnerPosition = -1
    private var arrayCageSpinnerPosition = -1
    private var isPhotoed = false
//    private val  TAKE_PHOTO_DOG = 1
    private lateinit var dogImage: ImageView
    private lateinit var photoFile: File
    private var photoUri = ""
    private var photoLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var fileUri = ""

        val sharedPref = this.getSharedPreferences(
            getString(com.example.doghotel.R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = resources.getString(com.example.doghotel.R.string.base_default_key)
        val baseVariant = sharedPref.getString(getString(com.example.doghotel.R.string.preference_file_key), defaultValue).toString()

        val sharedPref2 = this.getSharedPreferences(
            getString(com.example.doghotel.R.string.cage_numbers), Context.MODE_PRIVATE)
        val defaultCageNumbers = resources.getString(com.example.doghotel.R.string.cage_numbers)
        val cageNumber = sharedPref2.getString(getString(com.example.doghotel.R.string.cage_numbers), defaultCageNumbers).toString().toInt()

        val genderSpinner = binding.tvDogCardGender
        val cageSpinner = binding.spinnerCage
        val nickname = intent.extras?.getString("nick")

         // ****** Add photo ******

        photoFile = getPhotoFile("dog_")
        dogImage = binding.dogImageCard
        photoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK){
                Log.d("MyLog", "Photo add dog ${photoFile.absolutePath}")
                        Glide.with(this).load(photoFile.absoluteFile)
                            .error(com.example.doghotel.R.drawable.make_photo)
                            .into(binding.dogImageCard)
            }
        }

        dogImage.setOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val fileProvider = FileProvider.getUriForFile(this, "com.example.doghotel.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            try {
                photoLauncher?.launch(Intent(takePictureIntent))
             //   startActivityForResult(takePictureIntent, TAKE_PHOTO_DOG)
                isPhotoed = true
            }catch (e: ActivityNotFoundException){
                e.printStackTrace()
            }
            fileUri = photoFile.absolutePath
        }

        // ****** Filling fields *******
        if(nickname != null) {
            gender = intent.extras?.getString("gend")
            for (index in genderArray.indices){
                if (genderArray[index] == gender) {
                    arraySpinnerPosition = index
                }
            }

            val age = intent.extras!!.getString("ag")
            photoUri = intent.extras!!.getString("photoUri").toString()
            gender = intent.extras?.getString("gend")
            binding.tvDogCardNickname.text = Editable.Factory.getInstance().newEditable(nickname)
            binding.tvDogCardAge.text = Editable.Factory.getInstance().newEditable(age)


        }

        Glide.with(this).load(photoUri)
            .error(com.example.doghotel.R.drawable.make_photo)
            .into(binding.dogImageCard)


        // ***** Add items to database ******
        if (arraySpinnerPosition < 0){

            binding.btDogCardAdd.text = getString(com.example.doghotel.R.string.add_dog)
            binding.btDogCardAdd.setOnClickListener {
                if(binding.tvDogCardNickname.text.isEmpty()){
                    binding.tvDogCardNickname.requestFocus()
                }
                else {
                    if (binding.tvDogCardAge.text.isEmpty()) {
                        binding.tvDogCardAge.requestFocus()
                    }
                    if (binding.tvDogCardAge.text.toString().toInt() >= 30) {
                        Toast.makeText(
                            this,
                            "Dogs not live such long. Please enter correct age, not more 30 years",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                      } else{
                        val dog = Dog()
                        val nick = binding.tvDogCardNickname.text.toString()
                        dog.nickname = nickToUpperFirst(nick)
                        dog.gender = spinnerDogGenderChoice
                        dog.age = binding.tvDogCardAge.text.toString().toInt()
                        dog.days = Date().time
                        dog.cage = spinnerDogCageChoice.toInt()
                        dog.photo = photoFile.absolutePath

                        if (baseVariant == "SQLite") {

                            CoroutineScope(Dispatchers.IO).launch {

                                MainActivity.dbHelper.addDog(this@DogCardActivity, dog)
                            }
                        }
                        if (baseVariant == "Room") {

                            CoroutineScope(Dispatchers.IO).launch {
                                val mDogDatabase = DogDatabase.getDatabase(this@DogCardActivity)
                                mDogDatabase.dogDao().insertDog(dog)
                            }
                        }
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                        finish()
                    }

                }
            }

            // ****** Update card of item *******
        }else {
            binding.btDogCardAdd.text = getString(com.example.doghotel.R.string.update)
            binding.btDogCardDelete.isEnabled = true
            binding.btDogCardAdd.setOnClickListener{
                val dog = Dog()
                val position =  intent.extras!!.getInt("position")
                val photoUri = intent.extras!!.getString("photoUri")
                dog.dogID = position
                dog.nickname = binding.tvDogCardNickname.text.toString()
                dog.gender = genderSpinner.selectedItem.toString()
                dog.age = binding.tvDogCardAge.text.toString().toInt()
                dog.cage = cageSpinner.selectedItem.toString().toInt()

                dog.days = intent.extras!!.getString("days").toString().toLong()
                Log.d("MyLog", "days in card 2 ${intent.extras!!.getString("days").toString()}")
                if (isPhotoed) dog.photo = fileUri
                else dog.photo = photoUri.toString()
                Log.d("MyLog", "Uri photo is:  ${dog.photo}")
                Log.d("MyLog", "Uri photo is:  $fileUri")

                if (baseVariant == "SQLite") {
                    CoroutineScope(Dispatchers.IO).launch {

                        MainActivity.dbHelper.updateDog(dog)
                    }
                }
                if (baseVariant == "Room") {
                    CoroutineScope(Dispatchers.IO).launch {

                        val mDogDatabase = DogDatabase.getDatabase(this@DogCardActivity)
                        mDogDatabase.dogDao().updateDog(dog)
                    }
                }
                val i = Intent (this, MainActivity::class.java)
                startActivity(i)
                finish()
            }

        }

        // ***** Delete item function ******
        binding.btDogCardDelete.setOnClickListener{

        val dog = Dog()
        val position =  intent.extras!!.getInt("position")
            Log.d("MyLog", "Uri photo is:  $photoUri")

        val file = File(photoUri)
            file.delete()
            MediaScannerConnection.scanFile(this, arrayOf(file.toString()),
                arrayOf(file.name), null)

            Log.d("MyLog", "Uri photo is:  $photoUri")
            if (baseVariant == "SQLite") {
                CoroutineScope(Dispatchers.IO).launch {
                    MainActivity.dbHelper.deleteDog(position)
                }
            }
            if (baseVariant == "Room") {
                CoroutineScope(Dispatchers.IO).launch {
                    val mDogDatabase = DogDatabase.getDatabase(this@DogCardActivity)
                    dog.dogID = position
                    mDogDatabase.dogDao().deleteDog(dog)
                }
            }


            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }


        // ******* Gender spinner ********
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, genderArray)

        genderSpinner.adapter = arrayAdapter
        if (arraySpinnerPosition > -1) {
            genderSpinner.setSelection(arraySpinnerPosition, true)
 //           var spinnerPos = genderSpinner.setSelection(arraySpinnerPosition, true)
        }

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                spinnerDogGenderChoice = genderSpinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        // ***** Cage adapter *****

        val freeCageArray = getFreeCages(cageNumber)
        val arrayCageAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, freeCageArray)
        cageSpinner.adapter = arrayCageAdapter

        if (arrayCageSpinnerPosition > -1) {
            cageSpinner.setSelection(arrayCageSpinnerPosition, true)
            //    var spinnerPos = cageSpinner.setSelection(arraySpinnerPosition, true)
        }

        cageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                spinnerDogCageChoice = cageSpinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.btDogCardCancel.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    //******* Addition functions *********
    private fun nickToUpperFirst (string: String): String{
        return substring(string,0,1).uppercase()+substring(string, 1, string.length)
    }


    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    private fun getFreeCages (cages: Int): ArrayList<Int>{
        val freeCages: ArrayList<Int> = arrayListOf()
        val cageArray = MainActivity.dbHelper.getCageArray()

    //    val mDogDatabase = DogDatabase.getDatabase(this@DogCardActivity)
    //    val cageArray: ArrayList<Int> = mDogDatabase.dogDao().getCagesArray()


        cageArray.sort()

        for (i in 1..cages){
            var k = ""
            for (j in 0 until cageArray.size){
                if (i == cageArray[j]) k = "bingo"
            }
            if (k == "bingo") continue
            freeCages.add(i)
        }
         return freeCages
    }
}



