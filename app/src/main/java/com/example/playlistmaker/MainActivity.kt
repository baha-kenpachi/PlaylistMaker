package com.example.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val findButton = findViewById<Button>(R.id.find_button)

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "pressed find button", Toast.LENGTH_LONG).show()
            }
        }
        findButton.setOnClickListener(buttonClickListener)

        val mediaLibraryButton = findViewById<Button>(R.id.media_library_button)
        mediaLibraryButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "pressed media library button", Toast.LENGTH_SHORT).show()
        }
        val settingButton = findViewById<Button>(R.id.setting_button)
        settingButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "pressed setting button", Toast.LENGTH_LONG).show()
        }

    }
}