package com.example.playlistmaker.searchertrack

data class TrackData(
    val trackId : Number,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Number, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
)

