package com.example.doghotel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.doghotel.databinding.ActivitySortBinding

lateinit var binding: ActivitySortBinding

class SortActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySortBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSortDogNickname.setOnClickListener(){
           val i = Intent (this, MainActivity::class.java)
            i.putExtra("nickname", "1")
            startActivity(i)
            finishAffinity()
        }
        binding.btSortDogGender.setOnClickListener(){
            val i = Intent (this, MainActivity::class.java)
            i.putExtra("nickname", "2")
            startActivity(i)
            finishAffinity()
        }
        binding.btSortDogAge.setOnClickListener(){
            val i = Intent (this, MainActivity::class.java)
            i.putExtra("nickname", "3")
            startActivity(i)
            finishAffinity()
        }
        binding.btSortDogCage.setOnClickListener(){
            val i = Intent (this, MainActivity::class.java)
            i.putExtra("nickname", "4")
            startActivity(i)
            finishAffinity()
        }
        binding.btSortDogDays.setOnClickListener(){
            val i = Intent (this, MainActivity::class.java)
            i.putExtra("nickname", "5")
            startActivity(i)
            finishAffinity()
        }
        binding.btSortDogCancel.setOnClickListener(){
            val i = Intent (this, MainActivity::class.java)
            startActivity(i)
            finishAffinity()
        }


    }
}