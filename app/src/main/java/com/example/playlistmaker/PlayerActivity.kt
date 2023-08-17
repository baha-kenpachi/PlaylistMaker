package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.searchertrack.TrackData


class PlayerActivity : AppCompatActivity() {

    private lateinit var backButton:ImageButton
    private lateinit var albumCover: ImageView
    private lateinit var trackName: TextView
    private lateinit var trackAuthor: TextView
    private lateinit var timeCounter: TextView
    private lateinit var duration: TextView
    private lateinit var aalbum: TextView
    private lateinit var albumName: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        albumCover = findViewById(R.id.album_cover)
        trackName = findViewById(R.id.track_name)
        trackAuthor = findViewById(R.id.track_author)
        timeCounter = findViewById(R.id.time_counter)
        duration = findViewById(R.id.duration)
        aalbum = findViewById(R.id.album)
        albumName = findViewById(R.id.album_name)
        year = findViewById(R.id.year)
        genre = findViewById(R.id.genre)
        country = findViewById(R.id.country)

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val track = intent.getSerializableExtra("trackData") as? TrackData
        if (track != null) {
            if (track.collectionName.isNullOrEmpty()){
                aalbum.visibility = View.GONE
                albumName.visibility = View.GONE
            }
            bind(track)
            Log.d("TrackOnPlayer", "$track")
        }
    }
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
    private fun bind(track: TrackData) {

            Glide.with(this)
                .load(track.artworkUrl512)
                .placeholder(R.drawable.placeholder_not_found)
                .fitCenter()
                .transform(RoundedCorners(convertPixelsToDp(32.0f, this).toInt()))
                .into(albumCover)

            timeCounter.text = track.trackTime
            trackName.text = track.trackName
            trackAuthor.text = track.artistName
            duration.text = track.trackTime
            aalbum.text = track.collectionName
            year.text = track.releaseYear
            genre.text = track.primaryGenreName
            country.text = track.country

    }
}