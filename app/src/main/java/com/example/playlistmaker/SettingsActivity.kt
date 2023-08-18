package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    lateinit var backButton: ImageButton
    lateinit var themeSwitcher: SwitchMaterial
    lateinit var shareButton: ImageButton
    lateinit var supportButton: ImageButton
    lateinit var termsOfUseButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
        themeSwitcher = findViewById<SwitchMaterial>(R.id.swm_theme_switcher)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        themeSwitcher.isChecked = (applicationContext as App).darkTheme

        shareButton = findViewById<ImageButton>(R.id.button_share)
        shareButton.setOnClickListener {
            /* //Другой способ реализации share link
            val linkYPAndroidDeveloper = getString(R.string.link_to_yp_android_developer)
             val shareIntent = Intent(Intent.ACTION_SEND)
             shareIntent.type = "text/plain"
             shareIntent.putExtra(Intent.EXTRA_TEXT, linkYPAndroidDeveloper)
             startActivity(Intent.createChooser(shareIntent, "Share link"))*/
            Intent(Intent.ACTION_SEND).apply {
                // Получаем строковое значение ссылки на YP Android Developer из ресурсов
                val link = getString(R.string.link_to_yp_android_developer)
                type = "text/plain"
                // Добавляем текстовые данные (ссылку) в интент
                putExtra(Intent.EXTRA_TEXT, link)
                // Запускаем активность выбора приложения для поделиться
                startActivity(Intent.createChooser(this, "Share link"))
            }
        }

        supportButton = findViewById<ImageButton>(R.id.button_support)
        supportButton.setOnClickListener {
            // Создание нового интента для отправки письма
            Intent(Intent.ACTION_SENDTO).apply {
                // Получение темы и текста письма из ресурсов
                val letterTheme = getString(R.string.letter_theme_to_support)
                val letterText = getString(R.string.letter_text_to_support)
                // Установка URI схемы для отправки почты
                data = Uri.parse("mailto:")
                // Добавление адреса получателя, темы письма,текста письма
                putExtra(Intent.EXTRA_EMAIL, arrayOf("kenpachi03@yandex.ru"))
                putExtra(Intent.EXTRA_SUBJECT, letterTheme)
                putExtra(Intent.EXTRA_TEXT, letterText)
                startActivity(this)
            }
        }

        termsOfUseButton = findViewById<ImageButton>(R.id.button_arrow_forward)
        termsOfUseButton.setOnClickListener {
            // Получение ссылки на YP Offer из ресурсов
            val url = Uri.parse(getString(R.string.yp_offer))
            // Создание нового интента для открытия веб-страницы по данной ссылке
            Intent(Intent.ACTION_VIEW, url).apply {
                startActivity(this)
            }
        }
    }

    override fun onStop() {
        super.onStop()

        val sharedPrefsTheme = getSharedPreferences(Constants.THEME_SWITCHER_VALUE, MODE_PRIVATE)

        sharedPrefsTheme.edit()
            .putBoolean(Constants.THEME_SWITCHER_KEY, (applicationContext as App).darkTheme)
            .apply()
        Toast.makeText(
            this,
            "Saved theme value ${(applicationContext as App).darkTheme}",
            Toast.LENGTH_SHORT
        )
            .show()
    }
}

