package com.example.playlistmaker.searchertrack

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

//class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
class TrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.track_view, parentView, false)
) {
    private val ivTrack : ImageView = itemView.findViewById(R.id.ivTrack)
    private val tvTrackName : TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistName : TextView = itemView.findViewById(R.id.tvArtistName)
    private val tvTrackTime : TextView = itemView.findViewById(R.id.tvTrackTime)

    fun bind(item:TrackData){
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.clear_button)
            //.centerInside()
            .centerCrop()
            .transform(RoundedCorners(25))
            .into(ivTrack)

       val converter =  SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        tvTrackTime.text = converter

        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName


    }
}