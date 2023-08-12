package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

//значение и ключ для записи об недавнем поиске
const val RECENT_SEARCH_VALUE = "recent_search_value"
const val RECENT_SEARCH_KEY = "recent_search_key"

//значение и ключ для записи об изменении темы
const val THEME_SWITCHER_VALUE = "theme_switcher_value"
const val THEME_SWITCHER_KEY = "theme_switcher_key"
class App : Application() {
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        // Получаем экземпляр SharedPreferences, связанный с выбором темы
        val sharedPrefsTheme = getSharedPreferences(THEME_SWITCHER_VALUE, MODE_PRIVATE)
        // Получаем булевое значение выбора темы из SharedPreferences
        var themeMode = sharedPrefsTheme.getBoolean(THEME_SWITCHER_KEY, false)
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