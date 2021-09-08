package com.example.doghotel.sharedpref

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

class Prefs (context: Context) {
    val PREFS_FILENAME = "com.example.doghotel.prefs"
    val BASE_VARIANT = "base_variantr"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var baseVariant: String?
        get() = prefs.getString(BASE_VARIANT, "SQLite")
        set(value) = prefs.edit().putString(BASE_VARIANT, value).apply()
}

