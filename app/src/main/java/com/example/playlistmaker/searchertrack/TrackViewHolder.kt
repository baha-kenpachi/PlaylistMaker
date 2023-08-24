package com.example.playlistmaker.searchertrack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding

//class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
class TrackViewHolder(private val binding: TrackViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TrackData) {
        val context = itemView.context

        Glide.with(context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder_not_found)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(binding.ivTrack)

        binding.tvTrackTime.text = item.trackTime
        binding.tvTrackName.text = item.trackName ?: "-"
        binding.tvArtistName.text = item.artistName ?: "-"
    }

    companion object {
        fun create(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackViewBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }
}