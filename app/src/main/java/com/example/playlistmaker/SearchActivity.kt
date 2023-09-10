package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
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

class SearchActivity : AppCompatActivity(), RecentSearchHistory.OnTrackChangeObserver {
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
    private lateinit var savedTracksList: Array<TrackData>
    private lateinit var progressBar : ProgressBar

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { searchRequest() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPreferencesSearchHistory = getSharedPreferences(Constants.RECENT_SEARCH_VALUE, MODE_PRIVATE)
        recentSearchHistory = RecentSearchHistory(sharedPreferencesSearchHistory) // Инициализируем экземпляр класса RecentSearchTracks
        recentSearchHistory.onTrackChangeObserver = this

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
        progressBar = findViewById(R.id.progressBar)
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

        //setRecentSearchVisibility(isRecentSearchVisible)
        Log.d("OnItemClick_LOG", "Status code check llRecentSearch .add(): ${recentSearchHistory.get().isNotEmpty()}")
        setRecentSearchVisibility(recentSearchHistory.get().isNotEmpty())

        cleanBtn()

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setRecentSearchVisibility(false)
                clearButton.visibility = clearButtonVisibility(s)
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                setRecentSearchVisibility(false)// empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        //начинаем искать при нажатии поиска на клавиатуре
        /*inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                searchRequest()

                true
            }
            false
        }*/

        rvTracks = findViewById(R.id.rvTracks)
        recentSearchList = findViewById(R.id.rvRecentSearch)
        rvTracks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recentSearchList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackAdapter = TrackAdapter(trackList)
        rvTracks.adapter = trackAdapter

        val onClickTrack = {track: TrackData ->
            if (clickDebounce()) {
                recentSearchHistory.add(track)
                savedTrackAdapter.notifyDataSetChanged()
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra("trackData", track)
                startActivity(intent)
                Toast.makeText(this, "Saved track", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        trackAdapter.itemClickListener = onClickTrack


        showRecentSearchTrackList()
        clearRecentTrack()
        savedTrackAdapter.itemClickListener = onClickTrack

    }

    override fun onResume() {
        super.onResume()
        showAdapter()
    }

    private fun cleanBtn (){
        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)

            trackList.clear()
            trackAdapter.notifyDataSetChanged()

            //showRecentSearchTrackList()
            showAdapter()

            llNotConnection.visibility = View.GONE
            llNotFound.visibility = View.GONE
        }
    }
    override fun onTrackChange(items: MutableList<TrackData>) {
        savedTrackAdapter.updateItems(items)
        //savedTrackAdapter.notifyDataSetChanged()
        //showRecentSearchTrackList()
    }
    private fun showAdapter(){
        recentSearchList.adapter = savedTrackAdapter
        setRecentSearchVisibility(!savedTracksList.isNullOrEmpty())
    }
    private fun showRecentSearchTrackList(){
        savedTracksList = recentSearchHistory.get()
        val savedTrackList = savedTracksList.toList() // Преобразуем массив в список
        Log.d("SavedTrackList", "Status code saved track list: ${savedTrackList}")
        savedTrackAdapter = TrackAdapter(savedTrackList)
        showAdapter()
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
        outState.putString(Constants.PRODUCT_AMOUNT, savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        savedText = savedInstanceState.getString(Constants.PRODUCT_AMOUNT, "")
        inputEditText.setText(savedText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        val isInputEmpty = savedTracksList.isNullOrEmpty()
        setRecentSearchVisibility(isInputEmpty) // проверка ввода и управление видимостью llRecentSearch

        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    private fun setRecentSearchVisibility(isVisible: Boolean) {
        if (isVisible) {
            recentSearchContainer.visibility = View.VISIBLE
        } else {
            recentSearchContainer.visibility = View.GONE
        }
    }

    private fun searchRequest (){
        val searchText = inputEditText.text.toString()
        if (inputEditText.text.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            iTunesService
                .search(searchText)
                .enqueue(object :
                    Callback<TracksResponse> {
                    override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                        progressBar.visibility = View.GONE
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
                        progressBar.visibility = View.GONE
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

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun generateRandomNumber(): Int {
        return Random.nextInt(11)
    }



}