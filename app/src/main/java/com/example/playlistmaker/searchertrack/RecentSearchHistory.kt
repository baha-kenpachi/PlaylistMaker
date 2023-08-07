package com.example.playlistmaker.searchertrack

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.RECENT_SEARCH_KEY
import com.google.gson.Gson

//этот класс отвечает за вывод 10-ти сохранённых трэков, за добавление сохраненных треков  и за очистку списка
class RecentSearchHistory(private val sharedPrefs: SharedPreferences) {
    private var historyList : MutableList<TrackData> = mutableListOf()
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


        Log.d("OnItemClick_LOG", "Status code RecentSearchTrack .add(): ${historyList}")
        //записали в SharedPreferences
        sharedPrefs.edit()
            .putString(RECENT_SEARCH_KEY, createJsonFromTrackList(historyList))
            .apply()
    }

    fun get(): Array<TrackData> {
        return createTracksFromJson(
            sharedPrefs.getString(
                RECENT_SEARCH_KEY,
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



}