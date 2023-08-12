package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.searchertrack.RecentSearchHistory
import com.example.playlistmaker.searchertrack.TrackAdapter
import com.example.playlistmaker.searchertrack.TrackData
import com.example.playlistmaker.serchInApi.TracksResponse
import com.example.playlistmaker.serchInApi.iTunesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class SearchActivity : AppCompatActivity() {
    private val iTunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
   private val iTunesService = retrofit.create(iTunesApi::class.java)

    private lateinit var inputEditText: EditText
    private lateinit var backButton: ImageButton
    private lateinit var clearButton: ImageView
    private var savedText: String = ""


    private lateinit var rvTracks: RecyclerView
    private lateinit var recentSearchList: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var savedTrackAdapter: TrackAdapter
    private val trackList = ArrayList<TrackData>()
    private lateinit var llNotConnection : LinearLayout
    private lateinit var llNotFound : LinearLayout
    private lateinit var bRefresh : Button
    private lateinit var bClearHistory : Button
    private lateinit var recentSearchContainer: LinearLayout
    private lateinit var recentSearchHistory: RecentSearchHistory
    private var isRecentSearchVisible = true

    private companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
    }

    private lateinit var savedTracksList: Array<TrackData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPreferencesSearchHistory = getSharedPreferences(RECENT_SEARCH_VALUE, MODE_PRIVATE)
        recentSearchHistory = RecentSearchHistory(sharedPreferencesSearchHistory) // Инициализируем экземпляр класса RecentSearchTracks

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
        // для него пока ничего не реализовано
        recentSearchContainer = findViewById<LinearLayout>(R.id.llRecentSearch)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        bClearHistory = findViewById(R.id.bClearHistory)
        llNotConnection = findViewById<LinearLayout>(R.id.llNotConnection)
        llNotFound = findViewById<LinearLayout>(R.id.llNotFound)

        bRefresh = findViewById<Button>(R.id.bRefresh) // кнопка становится видимой
        bRefresh.setOnClickListener {
            // Повторно выполняем запрос
            searchRequest()
        }

        // Еще один способ вернуть данные
        /*if (savedInstanceState != null) {
            val savedText = savedInstanceState.getString(PRODUCT_AMOUNT)
            inputEditText.setText(savedText)
        }*/

        setRecentSearchVisibility(isRecentSearchVisible)
        Log.d("OnItemClick_LOG", "Status code check llRecentSearch .add(): ${recentSearchHistory.get().isNotEmpty()}")
        setRecentSearchVisibility(recentSearchHistory.get().isNotEmpty())

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)

            trackList.clear()
            trackAdapter.notifyDataSetChanged()

            savedTrackAdapter.notifyDataSetChanged()
            showRecentSearchTrackList()
            setRecentSearchVisibility(true) //делаем видимым список недавно проссмотренных

            llNotConnection.visibility = View.GONE
            llNotFound.visibility = View.GONE
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
        //начинаем искать при нажатии поиска на клавиатуре
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                searchRequest()

                true
            }
            false
        }

        rvTracks = findViewById(R.id.rvTracks)
        recentSearchList = findViewById(R.id.rvRecentSearch)
        rvTracks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recentSearchList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackAdapter = TrackAdapter(trackList)
        rvTracks.adapter = trackAdapter

        trackAdapter.itemClickListener = {track: TrackData -> recentSearchHistory.add(track)}

        showRecentSearchTrackList()
        clearRecentTrack()

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                isRecentSearchVisible = s.isNullOrEmpty()
                setRecentSearchVisibility(isRecentSearchVisible)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }
    private fun showRecentSearchTrackList(){
        savedTracksList = recentSearchHistory.get()
        val savedTrackList = savedTracksList.toList() // Преобразуем массив в список
        Log.d("SavedTrackList", "Status code saved track list: ${savedTrackList}")
        savedTrackAdapter = TrackAdapter(savedTrackList)
        //savedTrackAdapter = TrackAdapter(trackListData)
        recentSearchList.adapter = savedTrackAdapter
    }
    private fun clearRecentTrack(){
        bClearHistory.setOnClickListener {
            recentSearchHistory.clear()
            setRecentSearchVisibility(false)
        }
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
        val isInputEmpty = s.isNullOrEmpty()
        setRecentSearchVisibility(isInputEmpty) // проверка ввода и управление видимостью llRecentSearch

        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchRequest (){
        val searchText = inputEditText.text.toString()
        if (inputEditText.text.isNotEmpty()) {
            iTunesService
                .search(searchText)
                .enqueue(object :
                    Callback<TracksResponse> {
                    override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                        if (response.isSuccessful) {
                            // Запрос успешно выполнен
                            trackList.clear()
                            val tracks = response.body()?.results
                            if (tracks?.isNotEmpty() == true) {
                                trackList.addAll(tracks)
                                trackAdapter.notifyDataSetChanged()
                            }
                            if (trackList.isEmpty()) {
                                showMessage(getString(R.string.nothing_not_found), "")
                            } else {
                                showMessage("", "")
                            }
                        } else {
                            // Обработка ошибки
                            if (response.code() == 200) {
                                // В случае отсутствия сети
                                showMessageNotConnection(getString(R.string.not_connection), searchText)
                            } else {
                                // В случае другой ошибки
                                showMessage(getString(R.string.not_connection), searchText)
                            }
                        }
                    }

                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        showMessageNotConnection(getString(R.string.not_connection), t.message.toString())
                    }

                })
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            llNotFound.visibility = View.VISIBLE
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            //llNotFound.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
                Log.d("TRANSLATION_LOG", "Status code: ${additionalMessage}")
            }
        } else {
            llNotFound.visibility = View.GONE
        }
    }
    private fun showMessageNotConnection(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            llNotConnection.visibility = View.VISIBLE
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            //llNotFound.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
                Log.d("TRANSLATION_LOG", "Status code: ${additionalMessage}")
            }
        } else {
            llNotConnection.visibility = View.GONE
        }
    }
    private fun setRecentSearchVisibility(isVisible: Boolean) {
        if (isVisible) {
            recentSearchContainer.visibility = View.VISIBLE
        } else {
            recentSearchContainer.visibility = View.GONE
        }
    }
    fun generateRandomNumber(): Int {
        return Random.nextInt(11)
    }

}