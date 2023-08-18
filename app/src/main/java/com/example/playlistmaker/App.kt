package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        // Получаем экземпляр SharedPreferences, связанный с выбором темы
        val sharedPrefsTheme = getSharedPreferences(Constants.THEME_SWITCHER_VALUE, MODE_PRIVATE)
        // Получаем булевое значение выбора темы из SharedPreferences
        val themeMode = sharedPrefsTheme.getBoolean(Constants.THEME_SWITCHER_KEY, false)
        // Получаем доступ к контексту приложения в виде объекта ApplicationContext и вызываем метод switchTheme
        // у экземпляра класса App (предположительно), передавая ему значение выбора темы
        (applicationContext as App).switchTheme(themeMode)
    }

    // Функция для переключения темы приложения на основе переданного значения darkThemeEnabled
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        // Устанавливаем режим ночного режима для всего приложения, используя AppCompatDelegate.
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}