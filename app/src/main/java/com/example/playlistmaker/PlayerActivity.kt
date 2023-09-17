package com.example.playlistmaker

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.searchertrack.TrackData
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPlayerBinding.inflate(layoutInflater) }

    //private lateinit var binding: ActivityPlayerBinding
    //private val ibBackButton:ImageButton by lazy { findViewById(R.id.back_button)}
    private var playerState = Constants.STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()
    private lateinit var handler: Handler
    private val runnable = object : Runnable {
        override fun run() {
            binding.timeCounter.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            handler.postDelayed(this, Constants.TIME_COUNTER_DELAY)
        }
    }

    private val pixelsToDp = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        binding.backButton.setOnClickListener {
            finish()
        }


        val track = intent.serializable<TrackData>(Constants.TRACK_DATA)
        if (track != null) {
            if (track.collectionName.isNullOrEmpty()) {
                binding.album.visibility = View.GONE
                binding.albumName.visibility = View.GONE
            }
            bind(track)
            Log.d("TrackOnPlayer", "$track")
        }

        handler = Handler(Looper.getMainLooper())
        preparePlayer(track?.previewUrl ?: "-")

        binding.playerButtonPlayOrPause.setOnClickListener {
            playbackControl()
        }

    }
    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
            key,
            T::class.java
        )

        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    private fun bind(track: TrackData) {

        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder_not_found)
            .fitCenter()
            .transform(RoundedCorners(pixelsToDp.convertPixelsToDp(32.0f, this).toInt()))
            .into(binding.albumCover)

        binding.timeCounter.text = "00:00"
        binding.trackName.text = track.trackName ?: "-"
        binding.trackAuthor.text = track.artistName ?: "-"
        binding.duration.text = track.trackTime
        binding.album.text = track.collectionName
        binding.year.text = track.releaseYear ?: "-"
        binding.genre.text = track.primaryGenreName ?: "-"
        binding.country.text = track.country ?: "-"

    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = Constants.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playerButtonPlayOrPause.setImageResource(R.drawable.btn_play)
            playerState = Constants.STATE_PREPARED
            handler.removeCallbacks(runnable)
            binding.timeCounter.text = "00:00"
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playerButtonPlayOrPause.setImageResource(R.drawable.button_pause)
        playerState = Constants.STATE_PLAYING
        handler.post(runnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playerButtonPlayOrPause.setImageResource(R.drawable.btn_play)
        playerState = Constants.STATE_PAUSED
        handler.removeCallbacks(runnable)
    }

    private fun playbackControl() {
        when (playerState) {
            Constants.STATE_PLAYING -> {
                pausePlayer()
            }

            Constants.STATE_PREPARED, Constants.STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }

}