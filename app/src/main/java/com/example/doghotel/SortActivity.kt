package com.example.doghotel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SortActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sort)

        findViewById<Button>(R.id.bt_sort_dog_nickname).setOnClickListener(){
           val i = Intent (this, MainActivity::class.java)
            i.putExtra("nickname", "1")
            startActivity(i)
            finish()
        }
        findViewById<Button>(R.id.bt_sort_dog_gender).setOnClickListener(){
            val i = Intent (this, MainActivity::class.java)
            i.putExtra("nickname", "2")
            startActivity(i)
        }
        findViewById<Button>(R.id.bt_sort_dog_age).setOnClickListener(){
            val i = Intent (this, MainActivity::class.java)
            i.putExtra("nickname", "3")
            startActivity(i)
        }


    }
}