package com.example.playlistmaker.searchertrack

import android.content.SharedPreferences
import com.example.playlistmaker.Constants
import com.google.gson.Gson

//этот класс отвечает за вывод 10-ти сохранённых трэков, за добавление сохраненных треков  и за очистку списка
class RecentSearchHistory(private val sharedPrefs: SharedPreferences) {
    private var historyList : MutableList<TrackData> = mutableListOf()
    var onTrackChangeObserver : OnTrackChangeObserver? = null
    fun add(track: TrackData) {
        historyList = get().toMutableList()
        val existingIndex = historyList.indexOfFirst { it.trackId == track.trackId }
        if (existingIndex != -1) {
            historyList.removeAt(existingIndex)
        }
        historyList.add(0, track)
        if (historyList.size > 10) {
            historyList.removeLast()
        }
        //записали в SharedPreferences
        sharedPrefs.edit()
            .putString(Constants.RECENT_SEARCH_KEY, createJsonFromTrackList(historyList))
            .apply()
        onTrackChangeObserver?.onTrackChange(historyList)
    }

    fun get(): Array<TrackData> {
        return createTracksFromJson(
            sharedPrefs.getString(
                Constants.RECENT_SEARCH_KEY,
                null
            ) ?: return emptyArray()
        )
    }

    fun clear() {
        historyList.clear()
        sharedPrefs.edit()
            .clear()
            .apply()
    }

    private fun createJsonFromTrackList(tracks: List<TrackData>): String {
        return Gson().toJson(tracks)
    }

    private fun createTracksFromJson(json: String): Array<TrackData> {
        return Gson().fromJson(json, Array<TrackData>::class.java)
    }

    interface OnTrackChangeObserver {
        fun onTrackChange(items: MutableList<TrackData>)
    }


}