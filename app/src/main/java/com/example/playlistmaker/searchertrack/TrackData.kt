package com.example.playlistmaker.searchertrack

data class TrackData(
    val trackId : Number,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Number, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
)

/*var trackListData: List<TrackData> = listOf(
    TrackData(1, "Song 1", "Artist 1", 180000, "https://example.com/cover1.jpg"),
    TrackData(2, "Song 2", "Artist 2", 200000, "https://example.com/cover2.jpg"),
    TrackData(3, "Song 3", "Artist 3", 150000, "https://example.com/cover3.jpg"),
    TrackData(4, "Song 4", "Artist 4", 210000, "https://example.com/cover4.jpg"),
    TrackData(5, "Song 5", "Artist 5", 240000, "https://example.com/cover5.jpg"),
    TrackData(6, "Song 6", "Artist 6", 170000, "https://example.com/cover6.jpg"),
    TrackData(7, "Song 7", "Artist 7", 190000, "https://example.com/cover7.jpg"),
    TrackData(8, "Song 8", "Artist 8", 220000, "https://example.com/cover8.jpg"),
    TrackData(9, "Song 9", "Artist 9", 230000, "https://example.com/cover9.jpg"),
    TrackData(10, "Song 10", "Artist 10", 200000, "https://example.com/cover10.jpg")
)*/
