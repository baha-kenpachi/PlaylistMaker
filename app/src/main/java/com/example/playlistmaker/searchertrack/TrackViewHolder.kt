package com.example.playlistmaker.searchertrack

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val rootTrackLayout: ConstraintLayout = itemView.findViewById(R.id.rootTrackLayout)
    private val ivTrack : ImageView = itemView.findViewById(R.id.ivTrack)
    private val tvTrackName : TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistName : TextView = itemView.findViewById(R.id.tvArtistName)
    private val ivDotTrack : ImageView = itemView.findViewById(R.id.ivDotTrack)
    private val tvTrackTime : TextView = itemView.findViewById(R.id.tvTrackTime)
    private val ivButtonForward : ImageView = itemView.findViewById(R.id.ivButtonForward)

    fun bind(item:TrackData){
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.clear_button)
            .centerInside()
            //.centerCrop()
            .transform(RoundedCorners(25))
            .into(ivTrack)

//        ivTrack.setImageResource()
        tvTrackName.text = item.trackName
        tvArtistName.text = item.artistName
        tvTrackTime.text = item.trackTime

    }
}