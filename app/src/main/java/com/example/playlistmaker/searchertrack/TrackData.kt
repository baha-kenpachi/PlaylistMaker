package com.example.playlistmaker.searchertrack

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class TrackData(
    val trackId : Int,
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String = "",
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?
): Serializable {
    val trackTime: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    val artworkUrl512: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    val releaseYear: String?
        get() = releaseDate?.take(4)

}

