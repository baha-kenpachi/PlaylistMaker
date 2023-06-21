package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout

class FindActivity : AppCompatActivity() {
    private lateinit var inputEditText: EditText
    private lateinit var backButton: ImageButton
    private lateinit var clearButton: ImageView
    private var savedText : String = ""

    private companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
        // для него пока ничего не реализовано
        val linearLayout = findViewById<LinearLayout>(R.id.container)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)

        // Еще один способ вернуть данные
        /*if (savedInstanceState != null) {
            val savedText = savedInstanceState.getString(PRODUCT_AMOUNT)
            inputEditText.setText(savedText)
        }*/

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedText = inputEditText.text.toString()
        outState.putString(PRODUCT_AMOUNT, savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        savedText = savedInstanceState.getString(PRODUCT_AMOUNT, "")
        inputEditText.setText(savedText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}