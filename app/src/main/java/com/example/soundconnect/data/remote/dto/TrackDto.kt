package com.example.soundconnect.data.remote.dto

data class TrackDto(
    val id: String,
    val title: String,
    val artist: ArtistDto,
    val album: AlbumDto
)

data class ArtistDto(val name: String)
data class AlbumDto(val cover_medium: String)

data class SearchResponse(val data: List<TrackDto>)
