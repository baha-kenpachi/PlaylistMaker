package com.example.playlistmaker

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.searchertrack.TrackData
import com.example.playlistmaker.databinding.ActivityPlayerBinding


class PlayerActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPlayerBinding.inflate(layoutInflater) }
    //private lateinit var binding: ActivityPlayerBinding
    //private val ibBackButton:ImageButton by lazy { findViewById(R.id.back_button)}

    private val pixelsToDp = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.backButton.setOnClickListener {
            finish()
        }

        val track = intent.getSerializableExtra(Constants.TRACK_DATA) as? TrackData
        if (track != null) {
            if (track.collectionName.isNullOrEmpty()) {
                binding.album.visibility = View.GONE
                binding.albumName.visibility = View.GONE
            }
            bind(track)
            Log.d("TrackOnPlayer", "$track")
        }
    }

    private fun bind(track: TrackData) {

        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder_not_found)
            .fitCenter()
            .transform(RoundedCorners(pixelsToDp.convertPixelsToDp(32.0f, this).toInt()))
            .into(binding.albumCover)

        binding.timeCounter.text = track.trackTime
        binding.trackName.text = track.trackName ?: "-"
        binding.trackAuthor.text = track.artistName ?: "-"
        binding.duration.text = track.trackTime
        binding.album.text = track.collectionName
        binding.year.text = track.releaseYear  ?: "-"
        binding.genre.text = track.primaryGenreName  ?: "-"
        binding.country.text = track.country  ?: "-"

    }
}