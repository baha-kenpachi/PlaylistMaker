package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.swm_theme_switcher)
        val sharedPrefsTheme = getSharedPreferences(THEME_SWITCHER_VALUE, MODE_PRIVATE)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefsTheme.edit()
                .putBoolean(THEME_SWITCHER_KEY, checked)
                .apply()

            Toast.makeText(this, "Saved theme value ${checked}", Toast.LENGTH_SHORT)
                .show()
        }
        themeSwitcher.isChecked = (applicationContext as App).darkTheme


        val shareButton = findViewById<ImageButton>(R.id.button_share)
        shareButton.setOnClickListener {
            /* val linkYPAndroidDeveloper = getString(R.string.link_to_yp_android_developer)
             val shareIntent = Intent(Intent.ACTION_SEND)
             shareIntent.type = "text/plain"
             shareIntent.putExtra(Intent.EXTRA_TEXT, linkYPAndroidDeveloper)
             startActivity(Intent.createChooser(shareIntent, "Share link"))*/
            Intent(Intent.ACTION_SEND).apply {
                val linkYPAndroidDeveloper = getString(R.string.link_to_yp_android_developer)
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, linkYPAndroidDeveloper)
                startActivity(Intent.createChooser(this, "Share link"))
            }
        }

        val supportButton = findViewById<ImageButton>(R.id.button_support)
        supportButton.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                val letterTheme = getString(R.string.letter_theme_to_support)
                val letterText = getString(R.string.letter_text_to_support)
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("kenpachi03@yandex.ru"))
                putExtra(Intent.EXTRA_SUBJECT, letterTheme)
                putExtra(Intent.EXTRA_TEXT, letterText)
                startActivity(this)
            }
        }

        val termsOfUseButton = findViewById<ImageButton>(R.id.button_arrow_forward)
        termsOfUseButton.setOnClickListener {
            val url = Uri.parse(getString(R.string.yp_offer))
            Intent(Intent.ACTION_VIEW, url).apply {
                startActivity(this)
            }
        }
    }
}

