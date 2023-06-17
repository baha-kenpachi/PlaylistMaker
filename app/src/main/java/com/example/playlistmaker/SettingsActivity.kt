package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
        val shareButton = findViewById<ImageButton>(R.id.button_share)
        shareButton.setOnClickListener{
            val linkYPAndroidDeveloper = getString(R.string.link_to_yp_android_developer)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, linkYPAndroidDeveloper)
            startActivity(Intent.createChooser(shareIntent, "Share link"))
        }

        val supportButton = findViewById<ImageButton>(R.id.button_support)
            supportButton.setOnClickListener{
                val letterTheme = getString(R.string.letter_theme_to_support)
                val letterText = getString(R.string.letter_text_to_support)
                val shareIntent = Intent(Intent.ACTION_SENDTO)
                shareIntent.data = Uri.parse("mailto:")
                shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("kenpachi03@yandex.ru"))
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, letterTheme)
                shareIntent.putExtra(Intent.EXTRA_TEXT, letterText)
                startActivity(shareIntent)
            }

        val termsOfUseButton = findViewById<ImageButton>(R.id.button_arrow_forward)
        termsOfUseButton.setOnClickListener{
            val url = Uri.parse(getString(R.string.yp_offer))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }

    }
}