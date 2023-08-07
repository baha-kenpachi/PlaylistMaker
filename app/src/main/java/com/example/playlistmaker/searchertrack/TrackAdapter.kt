package com.example.playlistmaker.searchertrack

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private val data: List<TrackData>
) : RecyclerView.Adapter<TrackViewHolder>() {
    var itemClickListener: ((track: TrackData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
//        return TrackViewHolder(view)
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            if (itemClickListener != null) itemClickListener?.invoke(data[position])
            //смотрю что сохраняется при нажатии
            //Toast.makeText(holder.itemView.context, "Saved theme value ${data[position]}", Toast.LENGTH_SHORT)
            //    .show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}