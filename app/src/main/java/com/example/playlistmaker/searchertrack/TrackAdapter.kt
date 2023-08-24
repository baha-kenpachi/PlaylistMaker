package com.example.playlistmaker.searchertrack

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private var data: List<TrackData>
) : RecyclerView.Adapter<TrackViewHolder>() {
    var itemClickListener: ((track: TrackData) -> Unit)? = null

    fun updateItems(newItems: List<TrackData>) {
        val oldItems = data
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].trackId == newItems[newItemPosition].trackId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }

        })
        data = newItems

        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
//        return TrackViewHolder(view)
        return TrackViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            if (itemClickListener != null) itemClickListener?.invoke(data[position])
            //смотрю что сохраняется при нажатии
            /*Toast.makeText(holder.itemView.context, "Saved track", Toast.LENGTH_SHORT)
                .show()*/
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}