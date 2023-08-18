package com.example.playlistmaker.searchertrack

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

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
            .placeholder(R.drawable.placeholder_not_found)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(ivTrack)

        tvTrackTime.text = item.trackTime
        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName
    }
}