package com.example.doghotel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        var cageNumber = "50"
        var sharedPref = this.getSharedPreferences(
            getString(R.string.cage_numbers), Context.MODE_PRIVATE)
        findViewById<Button>(R.id.bt_setting_ok).setOnClickListener{
            cageNumber = findViewById<TextView>(R.id.tv_setting_number_cage).text.toString()
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString(getString(R.string.cage_numbers), cageNumber)
            editor.apply()

            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finishAffinity()
            }
        findViewById<Button>(R.id.bt_setting_cancel).setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finishAffinity()
        }


    }
}