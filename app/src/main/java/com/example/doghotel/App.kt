package com.example.doghotel

import android.app.Application
import com.example.doghotel.sharedpref.Prefs

//val prefs: Prefs by lazy { App.prefs!! }
class App: Application() {

    companion object{
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
    }
}

