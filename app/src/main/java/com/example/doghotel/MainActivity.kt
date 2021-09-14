package com.example.doghotel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Switch
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doghotel.databinding.ActivityMainBinding
import com.example.doghotel.db.room.DogDatabase
import com.example.doghotel.db.sqlite.DbHelper
import com.example.doghotel.model.Dog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList



class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var dbHelper: DbHelper
    }
     lateinit var binding: ActivityMainBinding
     lateinit var dogsList: ArrayList<Dog>
     private lateinit var mDogDatabase: DogDatabase
     var searchDogFilter: ArrayList<Dog> = ArrayList<Dog>()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dog)
        supportActionBar?.setCustomView(R.layout.switch_layout)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        var sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        //****** Base switcher *******

        findViewById<Switch>(R.id.switch_room).setOnCheckedChangeListener { _, isChecked ->

            val baseVariant: String
            if(isChecked) {
                baseVariant = "Room"
                findViewById<Switch>(R.id.switch_room).text = "Room"
            }
            else {
                baseVariant = "SQLite"
                findViewById<Switch>(R.id.switch_room).text = "SQLite"
            }
        //    Log.d("MyLog", "Переключились на $baseVariant")
            val editor:SharedPreferences.Editor = sharedPref.edit()
            editor.putString(getString(R.string.preference_file_key), baseVariant)
            editor.apply()

        }

        sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return
        val defaultValue = resources.getString(R.string.base_default_key)
        val baseVariant = sharedPref.getString(getString(R.string.preference_file_key), defaultValue).toString()

        if (baseVariant == "Room") {
            findViewById<Switch>(R.id.switch_room).text = "Room"
        }else {
            findViewById<Switch>(R.id.switch_room).text = "SQLite"
        }
   //     Log.d("MyLog", "В мэйн пришла база $baseVariant")

        dbHelper = DbHelper(this)

        CoroutineScope(Dispatchers.IO).launch {
            viewDogs(baseVariant)
        }

        findViewById<FloatingActionButton>(R.id.add_button).setOnClickListener {
            val i = Intent (this, DogCardActivity::class.java)
            startActivity(i)
            finishAffinity()
        }

        // ****** Search option ******
        binding.searchField.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (binding.searchField.text.isNotEmpty()){
                    searchDogFilter.clear()
                    for (i in 0 until dogsList.size){
                        if (dogsList[i].nickname.uppercase().contains(s.toString().uppercase()))
                            searchDogFilter.add(dogsList[i])
                    }
                    val adapter = DogRvAdapter(this@MainActivity, searchDogFilter)
                    val rv: RecyclerView = binding.rvDogs
                    rv.adapter = adapter

                    dogClickItems(searchDogFilter, adapter)
                }else{
                    val adapter = DogRvAdapter(this@MainActivity, dogsList)
                    val rv: RecyclerView = binding.rvDogs
                       rv.adapter = adapter
                    dogClickItems(dogsList, adapter)
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        )
        binding.btCloseSearch.setOnClickListener {
            val adapter = DogRvAdapter(this@MainActivity, dogsList)
            val rv: RecyclerView = binding.rvDogs
            rv.adapter = adapter
            dogClickItems(dogsList, adapter)
            if (binding.searchLayout.isVisible) binding.searchLayout.visibility = GONE
        }

    }

        // ******* Filling recycler items  and sorting *******
        private fun viewDogs(baseVariant: String)  {

        if (baseVariant == "SQLite") {
               dogsList = dbHelper.getDogs(this@MainActivity)
                 Log.d("MyLog", "Сработал SQLite")
        }
        else {
               mDogDatabase = DogDatabase.getDatabase(this@MainActivity)
               dogsList = mDogDatabase.dogDao().getAll() as ArrayList
               Log.d("MyLog", "Сработал Room")
        }

            if (intent.extras?.getString("nickname")=="1") dogsList.sortBy {it.nickname}
            if (intent.extras?.getString("nickname")=="2") dogsList.sortBy {it.gender}
            if (intent.extras?.getString("nickname")=="3") dogsList.sortBy {it.age}
            if (intent.extras?.getString("nickname")=="4") dogsList.sortBy {it.cage}
            if (intent.extras?.getString("nickname")=="5") dogsList.sortBy {it.days}
            val adapter = DogRvAdapter(this@MainActivity, dogsList)
            val rv: RecyclerView = binding.rvDogs
            rv.layoutManager = LinearLayoutManager(this@MainActivity)
            rv.adapter = adapter
            dogClickItems(dogsList, adapter)
    }

        // ****** Click items ******
        fun dogClickItems(clickedList: ArrayList<Dog>, adapter: DogRvAdapter){
            adapter.setOnClickDogListner(object : DogRvAdapter.onClickDogListner{
                override fun onItemClick(position: Int) {
                    val i = Intent(this@MainActivity, DogCardActivity::class.java)
                    i.putExtra("position", clickedList[position].dogID)
                    i.putExtra("nick", clickedList[position].nickname)
                    i.putExtra("gend", clickedList[position].gender)
                    i.putExtra("ag", clickedList[position].age.toString())
                    i.putExtra("photoUri", clickedList[position].photo)
                    i.putExtra("days", clickedList[position].days.toString() )

                    startActivity(i)
                    }
                })
        }


        //******* Toolbar option menu ******
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sort_dog_button -> {
                val i = Intent(this, SortActivity::class.java)
                startActivity(i)
            }
            R.id.search_dog_button -> {
                binding.searchLayout.visibility = VISIBLE
            }
            R.id.settings_dog_button -> {
                val i = Intent(this, Settings::class.java)
                startActivity(i)
            }
        }
        return true
    }
}