package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


const val THEME_SWITCHER_VALUE = "theme_switcher_value"
const val THEME_SWITCHER_KEY = "theme_switcher_key"
class App : Application(){
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
    }
    fun switchTheme(darkThemeEnabled : Boolean){



        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}